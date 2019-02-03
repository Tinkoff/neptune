package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public final class ToGetSingleCheckedObject {
    private ToGetSingleCheckedObject() {
        super();
    }

    private static <T, R> Function<T, R> checkedSingle(Function<T, R> function,
                                                       Predicate<? super R> condition,
                                                       @Nullable Duration waitingTime,
                                                       @Nullable Duration sleepingTime,
                                                       @Nullable Supplier<? extends RuntimeException> exceptionSupplier) {
        return ToGetConditionalHelper.fluentWaitFunction(t ->
                        ofNullable(function.apply(t)).map(r -> {
                            try {
                                if (ToGetConditionalHelper.notNullAnd(condition).test(r)) {
                                    return r;
                                }
                            }
                            catch (Throwable t1) {
                                ToGetConditionalHelper.printErrorAndFalse(t1);
                            }
                            return null;
                        }).orElse(null), waitingTime,
                sleepingTime, Objects::nonNull, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function function which should return some object
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
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
                                                  Supplier<? extends RuntimeException> exceptionSupplier) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime),
                ToGetConditionalHelper.checkSleepingTime(sleepingTime), ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime, Duration sleepingTime,
                                                  Supplier<? extends RuntimeException> exceptionSupplier) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime),
                ToGetConditionalHelper.checkSleepingTime(sleepingTime), ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function function which should return some object
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. Some exception is thrown if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Supplier<? extends RuntimeException> exceptionSupplier) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime),
                null, ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime,
                                                  Supplier<? extends RuntimeException> exceptionSupplier) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime),
                null, ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function function which should return some object
     * @param condition predicate which is used to find some target value
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. Some exception is thrown if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Supplier<? extends RuntimeException> exceptionSupplier) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), null,
                null, ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function function which should return some object
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. Some exception is thrown if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Supplier<? extends RuntimeException> exceptionSupplier) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, null,
                null, ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function function which should return some object
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime,
                                                  Duration sleepingTime) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime),
                ToGetConditionalHelper.checkSleepingTime(sleepingTime), null);
    }

    /**
     * This method returns a function. The result function returns a single value which differs from null.
     *
     * @param function function which should return some object
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
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime), ToGetConditionalHelper.checkSleepingTime(sleepingTime),
                null);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function function which should return some object
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition,
                                                  Duration waitingTime) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime),
                null, null);
    }

    /**
     * TThis method returns a function. The result function returns a single value which differs from null.
     *
     * @param function function which should return some object
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it differs from null. {@code null} is returned if value is null.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Duration waitingTime) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime),
                null,  null);
    }

    /**
     * This method returns a function. The result function returns a single value which
     * suits criteria.
     *
     * @param function which should return a value to check.
     * @param condition which is used to check the target value.
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single value.
     * It returns a value if it suits criteria. {@code null} is returned if value is null or doesn't suit criteria.
     */
    public static <T, R> Function<T, R> getSingle(Function<T, R> function,
                                                  Predicate<? super R> condition) {
        return checkedSingle(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), null, null, null);
    }
}
