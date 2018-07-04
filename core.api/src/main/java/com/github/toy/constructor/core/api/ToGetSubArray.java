package com.github.toy.constructor.core.api;

import org.apache.commons.lang3.ArrayUtils;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.AsIsPredicate.AS_IS;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.*;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public final class ToGetSubArray {

    private ToGetSubArray() {
        super();
    }

    private static <T, R> Function<T, R[]> array(Function<T, R[]> function,
                                                 Predicate<? super R> condition,
                                                 Duration waitingTime,
                                                 Duration sleepingTime,
                                                 boolean checkConditionInParallel,
                                                 boolean ignoreExceptionOnConditionCheck,
                                                 Supplier<? extends RuntimeException> exceptionSupplier) {
        return fluentWaitFunction(getDescription(EMPTY, function, condition), t ->
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
                waitingTime, sleepingTime, rs -> rs != null && rs.length > 0, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
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
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   boolean checkConditionInParallel,
                                                   boolean ignoreExceptionOnConditionCheck,
                                                   Supplier<? extends RuntimeException> exceptionSupplier) {
        return array(checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime), checkSleepingTime(sleepingTime),
                checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
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
                                                   Supplier<? extends RuntimeException> exceptionSupplier) {
        return array(checkFunction(function), AS_IS, checkWaitingTime(waitingTime), checkSleepingTime(sleepingTime),
                true, true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   boolean checkConditionInParallel,
                                                   boolean ignoreExceptionOnConditionCheck,
                                                   Supplier<? extends RuntimeException> exceptionSupplier) {
        return array(checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime), null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Supplier<? extends RuntimeException> exceptionSupplier) {
        return array(checkFunction(function), AS_IS, checkWaitingTime(waitingTime), null,
                true, true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   boolean checkConditionInParallel,
                                                   boolean ignoreExceptionOnConditionCheck,
                                                   Supplier<? extends RuntimeException> exceptionSupplier) {
        return array(checkFunction(function), checkCondition(condition), null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return an array
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Supplier<? extends RuntimeException> exceptionSupplier) {
        return array(checkFunction(function), AS_IS, null, null,
                true, true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
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
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   boolean checkConditionInParallel,
                                                   boolean ignoreExceptionOnConditionCheck) {
        return array(checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime), checkSleepingTime(sleepingTime),
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
        return array(checkFunction(function), AS_IS, checkWaitingTime(waitingTime), checkSleepingTime(sleepingTime),
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
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
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   boolean checkConditionInParallel,
                                                   boolean ignoreExceptionOnConditionCheck) {
        return array(checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime), null,
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
        return array(checkFunction(function), AS_IS, checkWaitingTime(waitingTime), null,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
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
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   boolean checkConditionInParallel,
                                                   boolean ignoreExceptionOnConditionCheck) {
        return array(checkFunction(function), checkCondition(condition), null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }
}
