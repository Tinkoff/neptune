package com.github.toy.constructor.core.api;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.AsIsPredicate.AS_IS;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.*;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public final class ToGetSingleCheckedObject {
    private ToGetSingleCheckedObject() {
        super();
    }

    private static <T, R> Function<T, R> checkedSingle(Function<T, R> function,
                                                       Predicate<? super R> condition,
                                                       @Nullable Duration waitingTime,
                                                       @Nullable Duration sleepingTime,
                                                       boolean ignoreExceptionOnConditionCheck,
                                                       @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return fluentWaitFunction(getDescription(EMPTY, function, condition), t ->
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

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
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
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Duration sleepingTime,
                                                  boolean ignoreExceptionOnConditionCheck,
                                                  Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(function, condition, waitingTime,
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
        return checkedSingle(function, AS_IS, waitingTime,
                sleepingTime, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
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
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  boolean ignoreExceptionOnConditionCheck,
                                                  Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(function, condition, waitingTime,
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
        return checkedSingle(function, AS_IS, waitingTime,
                null, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function described function which should return some object
     * @param condition described predicate which is used to find some target value
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
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  boolean ignoreExceptionOnConditionCheck,
                                                  Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(function, condition, null,
                null, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function described function which should return some object
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkExceptionSupplier(exceptionOnTimeOut);
        return checkedSingle(function, AS_IS, null,
                null, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
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
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Duration sleepingTime,
                                                  boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return checkedSingle(function, condition, waitingTime,
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
        return checkedSingle(function, AS_IS, waitingTime,
                sleepingTime, true, null);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
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
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        return checkedSingle(function, condition, waitingTime,
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
        return checkedSingle(function, AS_IS, waitingTime,
                null, true, null);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
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
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        return checkedSingle(function, condition,
                null, null, ignoreExceptionOnConditionCheck, null);
    }
}
