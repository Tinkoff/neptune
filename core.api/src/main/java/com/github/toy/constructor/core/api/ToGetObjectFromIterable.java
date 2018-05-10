package com.github.toy.constructor.core.api;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.ToGetConditionalHelper.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;

public final class ToGetObjectFromIterable {

    private ToGetObjectFromIterable() {
        super();
    }

    private static <T, R, V extends Iterable<R>> Function<T, R> singleFromIterable(String description,
                                                                                   Function<T, V> function,
                                                                                   Predicate<? super R> condition,
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
                                                                               Predicate<? super R> condition,
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
        return singleFromIterable(description, function, AS_IS, waitingTime, sleepingTime, false,
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
                                                                               Predicate<? super  R> condition,
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
        return singleFromIterable(description, function, AS_IS, waitingTime, null, false,
                true, exceptionOnTimeOut);
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
                                                                               Predicate<? super R> condition,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck,
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkExceptionSupplier(exceptionOnTimeOut);
        return singleFromIterable(description, function, condition, null, null, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function described function which should return {@link Iterable}
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
                                                                               Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkExceptionSupplier(exceptionOnTimeOut);
        return singleFromIterable(description, function, AS_IS, null, null,
                false, true, exceptionOnTimeOut);
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
                                                                               Predicate<? super R> condition,
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
        return singleFromIterable(description, function, AS_IS, waitingTime, sleepingTime, false,
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
                                                                               Predicate<? super R> condition,
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
        return singleFromIterable(description, function, AS_IS, waitingTime, null, false,
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
                                                                               Predicate<? super R> condition,
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
        return singleFromIterable(description, function, AS_IS, null, null, false,
                true, null);
    }
}
