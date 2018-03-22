package com.github.toy.constructor.core.api;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofMillis;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

/**
 * This is the util which helps to crate function with given condition.
 */
public final class ToGetConditionalHelper {

    private ToGetConditionalHelper() {
        super();
    }

    private static <T> Predicate<T> asIs() {
        return condition("as is", t -> true);
    }

    private static <T> Predicate<T> notNullAnd(Predicate<T> condition) {
        return ((Predicate<T>) condition("is not null value", t -> true))
                .and(condition);
    }

    private static boolean returnFalseOrThrowException(Throwable t, boolean ignoreExceptionOnConditionCheck) {
        String message = format("%s was caught. Meassage: %s", t.getClass().getName(), t.getMessage());
        if (!ignoreExceptionOnConditionCheck) {
            throw new CheckConditionException(message, t);
        }

        System.err.println(message);
        t.printStackTrace();
        return false;
    }

    private static void checkCondition(Predicate<?> condition) {
        checkArgument(condition != null, "Predicate is not defined.");
        checkArgument(DescribedPredicate.class.isAssignableFrom(condition.getClass()),
                "Condition is not described. " +
                        "Use StoryWriter.condition to describe it.");
    }

    private static void checkFunction(Function<?, ?> function) {
        checkArgument(function != null, "Function is not defined");
        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Function is not described." +
                        " Use StoryWriter.toGet to describe it.");
    }

    private static void checkWaitingTime(Duration duration) {
        checkArgument(duration != null, "Time of the waiting for some " +
                "valuable result is not defined");
    }

    private static void checkSleepingTime(Duration duration) {
        checkArgument(duration != null, "Time of the sleeping is not defined");
    }

    private static void checkExceptionSupplier(Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkArgument(exceptionOnTimeOut != null,
                "Supplier of an exception to be thrown is not defined");
    }

    private static String getDescription(String description, Function<?, ?> function, Predicate<?> condition) {
        if (!isBlank(description)) {
            return format("%s from (%s) on condition %s", description, function, condition);
        }
        return format("%s on condition %s", function, condition);
    }

    private static <T, F> Function<T, F> fluentWaitFunction(String description,
                                                            Function<T, F> originalFunction,
                                                            @Nullable Duration waitingTime,
                                                            @Nullable Duration sleepingTime,
                                                            Predicate<F> till,
                                                            @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        String fullDescription = description;
        if (waitingTime != null) {
            fullDescription = format("%s. Time to get valuable result: %s", fullDescription,
                    formatDuration(waitingTime.toMillis(), "H:mm:ss:SSS", true));
        }
        Duration timeOut = ofNullable(waitingTime).orElseGet(() -> ofMillis(0));
        Duration sleeping = ofNullable(sleepingTime).orElseGet(() -> ofMillis(0));

        return toGet(fullDescription, t -> {
            long currentMillis = currentTimeMillis();
            long endMillis = currentMillis + timeOut.toMillis();

            F f = null;
            while (currentTimeMillis() <= endMillis + 100 && !till.test(f)) {
                f = originalFunction.apply(t);
                try {
                    sleep(sleeping.toMillis());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }

            return (F) ofNullable(exceptionOnTimeOut).map(exceptionSupplier1 -> {
                throw exceptionOnTimeOut.get();
            }).orElse(f);
        });
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable} which
     * suits criteria. The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns some value if something that suits criteria is found. Some exception is thrown otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        return fluentWaitFunction(getDescription(description, function, notNullAnd(condition)), t ->
                        ofNullable(function.apply(t))
                                .map(v -> stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                                    try {
                                        return condition.test(r);
                                    } catch (Throwable t1) {
                                        return returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                    }
                                }).findFirst().orElse(null))
                                .orElse(null),
                waitingTime, sleepingTime, Objects::nonNull, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     * The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}
     * which differs from null. Some exception is thrown otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return getFromIterable(description, function, asIs(), waitingTime,
                sleepingTime, true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable} which
     * suits criteria. The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns some value if something that suits criteria is found. Some exception is thrown otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               Duration waitingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return getFromIterable(description,
                function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     * The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}
     * which differs from null. Some exception is thrown otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return getFromIterable(description, function, asIs(), waitingTime,
                null, true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable} which
     * suits criteria. The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns some value if something that suits criteria is found. {@code null} is returned otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return getFromIterable(description,
                function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     * The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}
     * which differs from null. {@code null} is returned otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime) {
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return getFromIterable(description,
                function, asIs(), waitingTime, sleepingTime,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable} which
     * suits criteria. The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns some value if something that suits criteria is found. {@code null} is returned otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               Duration waitingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        checkWaitingTime(waitingTime);
        return getFromIterable(description,
                function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     * The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}
     * which differs from null. {@code null} is returned otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime) {
        checkWaitingTime(waitingTime);
        return getFromIterable(description,
                function, asIs(), waitingTime, null,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable} which
     * suits criteria. The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns some value if something that suits criteria is found. {@code null} is returned otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        return getFromIterable(description,
                function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     * The original function should return iterable.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}
     * which differs from null. {@code null} is returned otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function) {
        return getFromIterable(description,
                function, asIs(), null, null,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * The result function will return some value if something is found. Some exception is thrown otherwise.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck,
                                                     Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkArgument(function != null, "Function which should return array is not defined");
        checkArgument(condition != null, "Predicate which should be used as the condition to check " +
                "value from array is not defined.");

        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Function which should return array is not described." +
                        " Use StoryWriter.toGet to describe it.");
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                condition, waitingTime, sleepingTime, checkConditionInParallel, ignoreExceptionOnConditionCheck,
                exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * The result function will return some value if something is found. Some exception is thrown otherwise.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck,
                                                     Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return getFromArray(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * The result function will return some value if something is found. {@code null} is returned otherwise.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        return getFromArray(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * The result function will return some value if something is found. {@code null} is returned otherwise.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        return getFromArray(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * The result function will return some value if something is found. {@code null} is returned otherwise.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        return getFromArray(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);

    }

    /**
     * This method returns a function which returns a single value.
     * The original function should return a value to check.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return some object
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single checked value. The result function will return some
     * value if the check is passed successfully. Some exception is thrown otherwise.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             Duration sleepingTime,
                                                             boolean ignoreExceptionOnConditionCheck,
                                                             Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkArgument(function != null, "Function which should return object is not defined");
        checkArgument(condition != null, "Predicate which should be used as the condition to " +
                "check returned object is not defined.");

        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Function which should return object is not described." +
                        " Use StoryWriter.toGet to describe it.");
        checkCondition(condition);
        return fluentWaitFunction(getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(r -> {
                            try {
                                if (condition.test(r)) {
                                    return r;
                                }
                            }
                            catch (Throwable t1) {
                                returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                            }
                            return null;
                        }).orElse(null), waitingTime,
                sleepingTime, Objects::nonNull, exceptionOnTimeOut);
    }

    /**
     * This method returns a function which returns a single value.
     * The original function should return a value to check.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return some object
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single checked value. The result function will return some
     * value if the check is passed successfully. Some exception is thrown otherwise.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             boolean ignoreExceptionOnConditionCheck,
                                                             Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return getSingleOnCondition(description, function, condition,
                waitingTime, null, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function which returns a single value.
     * The original function should return a value to check.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return some object
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single checked value. The result function will return some
     * value if the check is passed successfully. {@code null} is returned otherwise.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             Duration sleepingTime,
                                                             boolean ignoreExceptionOnConditionCheck) {
        return getSingleOnCondition(description, function, condition,
                waitingTime, sleepingTime, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function which returns a single value.
     * The original function should return a value to check.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return some object
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single checked value. The result function will return some
     * value if the check is passed successfully. {@code null} is returned otherwise.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             boolean ignoreExceptionOnConditionCheck) {
        return getSingleOnCondition(description, function, condition,
                waitingTime, null, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function which returns a single value.
     * The original function should return a value to check.
     *
     * @param description of a value which should be returned.
     * @param function which should return a value to check.
     * @param condition which is used to check the target value.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single checked value. The result function will return some
     * value if the check is passed successfully. {@code null} is returned otherwise.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             boolean ignoreExceptionOnConditionCheck) {
        return getSingleOnCondition(description, function, condition,
                null, null, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns found values from {@link Iterable}.
     * The original function should return iterable to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns found values from {@link Iterable}.
     * The result function will return values if something is found. Some exception is thrown otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              Duration sleepingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck,
                                                                              Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkArgument(function != null, "Function which should return iterable is not defined");
        checkArgument(condition != null, "Predicate which should be used as the condition to " +
                "filter values from iterable is not defined.");

        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Function which should return iterable is not described." +
                        " Use StoryWriter.toGet to describe it.");
        checkCondition(condition);
        return fluentWaitFunction(getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(v -> {
                            List<R> result = stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                                try {
                                    return !condition.test(r);
                                } catch (Throwable t1) {
                                    return !returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                }
                            }).collect(toList());

                            Iterables.removeAll(v, result);
                            return v;
                        }).orElse(null),
                waitingTime, sleepingTime, v -> v != null && Iterables.size(v) > 0, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns found values from {@link Iterable}.
     * The original function should return iterable to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns found values from {@link Iterable}.
     * The result function will return values if something is found. Some exception is thrown otherwise.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck,
                                                                              Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return getSubIterable(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns found values from {@link Iterable}.
     * The original function should return iterable to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns found values from {@link Iterable}.
     * The result function will return values if something is found. Empty {@link Iterable} or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              Duration sleepingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        return getSubIterable(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns found values from {@link Iterable}.
     * The original function should return iterable to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns found values from {@link Iterable}.
     * The result function will return values if something is found. Empty {@link Iterable} or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        return getSubIterable(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns found values from {@link Iterable}.
     * The original function should return iterable to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns found values from {@link Iterable}.
     * The result function will return values if something is found. Empty {@link Iterable} or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        return getSubIterable(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Some exception is thrown otherwise.
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      Duration sleepingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck,
                                                      Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkArgument(function != null, "Function which should return array is not defined");
        checkArgument(condition != null, "Predicate which should be used as the condition to " +
                "filter values from array is not defined.");

        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Function which should return array is not described." +
                        " Use StoryWriter.toGet to describe it.");
        checkCondition(condition);
        return fluentWaitFunction(getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(rs -> {
                            List<R> subResult = stream(asList(rs).spliterator(), checkConditionInParallel).filter(r -> {
                                try {
                                    return !condition.test(r);
                                } catch (Throwable t1) {
                                    return !returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                }
                            }).collect(toList());

                            R[] result = rs;
                            for (R r: subResult) {
                                result = ArrayUtils.removeElement(result, r);
                            }
                            return result;
                        }).orElse(null),
                waitingTime, sleepingTime, rs -> rs != null && rs.length > 0, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Some exception is thrown otherwise.
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck,
                                                      Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return getSubArray(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Empty array or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      Duration sleepingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck) {
        return getSubArray(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Empty array or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck) {
        return getSubArray(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
     * @param description of a value which should be returned
     * @param function which should return an array.
     * @param condition which is used to find target values.
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Empty array or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        return getSubArray(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }


    private static class CheckConditionException extends RuntimeException {
        CheckConditionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
