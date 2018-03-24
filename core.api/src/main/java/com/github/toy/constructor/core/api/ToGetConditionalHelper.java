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
import static org.apache.commons.lang3.StringUtils.EMPTY;
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
        checkArgument(function != null, "Function is not defined.");
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

    private static <T, R, V extends Iterable<R>> Function<T, R> singleFromIterable(String description,
                                                                                   Function<T, V> function,
                                                                                   Predicate<R> condition,
                                                                                   @Nullable Duration waitingTime,
                                                                                   @Nullable Duration sleepingTime,
                                                                                   boolean checkConditionInParallel,
                                                                                   boolean ignoreExceptionOnConditionCheck,
                                                                                   @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return fluentWaitFunction(getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t))
                                .map(v -> stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                                    try {
                                        return notNullAnd(condition).test(r);
                                    } catch (Throwable t1) {
                                        return returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                    }
                                }).findFirst().orElse(null))
                                .orElse(null),
                waitingTime, sleepingTime, Objects::nonNull, exceptionOnTimeOut);
    }

    private static <T, R> Function<T, R> checkedSingle(String description,
                                                       Function<T, R> function,
                                                       Predicate<R> condition,
                                                       @Nullable Duration waitingTime,
                                                       @Nullable Duration sleepingTime,
                                                       boolean ignoreExceptionOnConditionCheck,
                                                       @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return fluentWaitFunction(getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(r -> {
                            try {
                                if (notNullAnd(condition).test(r)) {
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

    private static <T, R, V extends Iterable<R>> Function<T, V> iterable(String description,
                                                                        Function<T, V> function,
                                                                        Predicate<R> condition,
                                                                        @Nullable Duration waitingTime,
                                                                        @Nullable Duration sleepingTime,
                                                                        boolean checkConditionInParallel,
                                                                        boolean ignoreExceptionOnConditionCheck,
                                                                        @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return fluentWaitFunction(getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(v -> {
                            List<R> result = stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                                try {
                                    return !notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return !returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                }
                            }).collect(toList());

                            Iterables.removeAll(v, result);
                            return v;
                        }).orElse(null),
                waitingTime, sleepingTime, v -> v != null && Iterables.size(v) > 0, exceptionOnTimeOut);

    }

    private static <T, R> Function<T, R[]> array(String description,
                                                 Function<T, R[]> function,
                                                 Predicate<R> condition,
                                                 Duration waitingTime,
                                                 Duration sleepingTime,
                                                 boolean checkConditionInParallel,
                                                 boolean ignoreExceptionOnConditionCheck,
                                                 Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return fluentWaitFunction(getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(rs -> {
                            List<R> subResult = stream(asList(rs).spliterator(), checkConditionInParallel).filter(r -> {
                                try {
                                    return !notNullAnd(condition).test(r);
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
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
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
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return singleFromIterable(description, function, condition, waitingTime, sleepingTime, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
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
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return singleFromIterable(description, function, asIs(), waitingTime, sleepingTime, true,
                true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               Duration waitingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return singleFromIterable(description, function, condition, waitingTime, null, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return singleFromIterable(description, function, asIs(), waitingTime, null, true,
                true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return singleFromIterable(description, function, condition, waitingTime, sleepingTime, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return singleFromIterable(description, function, asIs(), waitingTime, sleepingTime, true,
                true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               Duration waitingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        return singleFromIterable(description, function, condition, waitingTime, null, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        return singleFromIterable(description, function, asIs(), waitingTime, null, true,
                true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<R> condition,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        return singleFromIterable(description, function, condition, null, null, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function) {
        checkFunction(function);
        return singleFromIterable(description, function, asIs(), null, null, true,
                true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
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
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck,
                                                     Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                condition, waitingTime, sleepingTime, checkConditionInParallel, ignoreExceptionOnConditionCheck,
                exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                waitingTime, sleepingTime, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
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
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck,
                                                     Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))), condition,
                waitingTime, checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                waitingTime, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
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
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                condition, waitingTime, sleepingTime, checkConditionInParallel, ignoreExceptionOnConditionCheck);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Duration sleepingTime) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                waitingTime, sleepingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
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
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     Duration waitingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                condition, waitingTime, checkConditionInParallel, ignoreExceptionOnConditionCheck);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Duration waitingTime) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                waitingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
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
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))),
                condition, checkConditionInParallel, ignoreExceptionOnConditionCheck);

    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function) {
        checkFunction(function);
        return getFromIterable(description, toGet(function.toString(), t -> asList(function.apply(t))));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
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
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. Some exception is thrown if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             Duration sleepingTime,
                                                             boolean ignoreExceptionOnConditionCheck,
                                                             Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(description, function, condition, waitingTime,
                sleepingTime, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function described function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime, Duration sleepingTime,
                                                  Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(EMPTY, function, asIs(), waitingTime,
                sleepingTime, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
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
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. Some exception is thrown if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             boolean ignoreExceptionOnConditionCheck,
                                                             Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(description, function, condition, waitingTime,
                null, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function described function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime,
                                                  Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(EMPTY, function, asIs(), waitingTime,
                null, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
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
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             Duration sleepingTime,
                                                             boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return checkedSingle(description, function, condition, waitingTime,
                sleepingTime, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function described function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. {@code null} is returned if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime,
                                                  Duration sleepingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return checkedSingle(EMPTY, function, asIs(), waitingTime,
                sleepingTime, true, null);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
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
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             Duration waitingTime,
                                                             boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        return checkedSingle(description, function, condition, waitingTime,
                null, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * TThis method returns a function. The result function returns a single value which differs from null.
     *
     * @param function described function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. {@code null} is returned if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        return checkedSingle(EMPTY, function, asIs(), waitingTime,
                null, true, null);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param description of a value which should be returned.
     * @param function which should return a value to check.
     * @param condition which is used to check the target value.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingleOnCondition(String description,
                                                             Function<T, R> function,
                                                             Predicate<R> condition,
                                                             boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        return checkedSingle(description, function, condition,
                null, null, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              Duration sleepingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck,
                                                                              Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime,
                                                                           Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(EMPTY, function, asIs(), waitingTime, sleepingTime,
                true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck,
                                                                              Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(EMPTY, function, asIs(), waitingTime, null,
                true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              Duration sleepingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return iterable(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return iterable(EMPTY, function, asIs(), waitingTime, sleepingTime,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              Duration waitingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        return iterable(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        return iterable(EMPTY, function, asIs(), waitingTime, null,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<R> condition,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        return iterable(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      Duration sleepingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck,
                                                      Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return array(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return array(EMPTY, function, asIs(), waitingTime, sleepingTime,
                true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck,
                                                      Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return array(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return array(EMPTY, function, asIs(), waitingTime, null,
                true, true, exceptionOnTimeOut);
    }



    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      Duration sleepingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return array(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Duration sleepingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return array(EMPTY, function, asIs(), waitingTime, sleepingTime,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
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
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getSubArray(String description,
                                                      Function<T, R[]> function,
                                                      Predicate<R> condition,
                                                      Duration waitingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        return array(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        return array(EMPTY, function, asIs(), waitingTime, null,
                true, true, null);
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
        checkFunction(function);
        checkCondition(condition);
        return array(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }


    private static class CheckConditionException extends RuntimeException {
        CheckConditionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
