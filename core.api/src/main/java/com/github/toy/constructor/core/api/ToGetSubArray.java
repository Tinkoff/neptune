package com.github.toy.constructor.core.api;

import org.apache.commons.lang3.ArrayUtils;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public final class ToGetSubArray {

    private ToGetSubArray() {
        super();
    }

    private static <T, R> Function<T, R[]> array(String description,
                                                 Function<T, R[]> function,
                                                 Predicate<? super R> condition,
                                                 Duration waitingTime,
                                                 Duration sleepingTime,
                                                 boolean checkConditionInParallel,
                                                 boolean ignoreExceptionOnConditionCheck,
                                                 Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return ToGetConditionalHelper.fluentWaitFunction(ToGetConditionalHelper.getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(rs -> {
                            List<R> subResult = stream(asList(rs).spliterator(), checkConditionInParallel).filter(r -> {
                                try {
                                    return !ToGetConditionalHelper.notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return !ToGetConditionalHelper.returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
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
                                                      Predicate<? super R> condition,
                                                      Duration waitingTime,
                                                      Duration sleepingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck,
                                                      Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkCondition(condition);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkSleepingTime(sleepingTime);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
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
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkSleepingTime(sleepingTime);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return array(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, sleepingTime,
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
                                                      Predicate<? super R> condition,
                                                      Duration waitingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck,
                                                      Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkCondition(condition);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
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
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return array(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, null,
                true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return an array
     * @param condition described predicate which is used to find some target value
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
                                                      Predicate<? super R> condition,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck,
                                                      Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkCondition(condition);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return array(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function described function which should return an array
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return array(EMPTY, function, ToGetConditionalHelper.asIs(), null, null,
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
                                                      Predicate<? super R> condition,
                                                      Duration waitingTime,
                                                      Duration sleepingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck) {
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkCondition(condition);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkSleepingTime(sleepingTime);
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
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkSleepingTime(sleepingTime);
        return array(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, sleepingTime,
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
                                                      Predicate<? super R> condition,
                                                      Duration waitingTime,
                                                      boolean checkConditionInParallel,
                                                      boolean ignoreExceptionOnConditionCheck) {
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkCondition(condition);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
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
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkWaitingTime(waitingTime);
        return array(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, null,
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
                                                     Predicate<? super R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        ToGetConditionalHelper.checkFunction(function);
        ToGetConditionalHelper.checkCondition(condition);
        return array(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }
}
