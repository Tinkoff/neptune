package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromIterable.getFromIterable;
import static java.util.Arrays.asList;

public final class ToGetObjectFromArray {

    private ToGetObjectFromArray() {
        super();
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), ToGetConditionalHelper.checkCondition(condition), waitingTime, sleepingTime,
                exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), waitingTime, sleepingTime, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), condition, waitingTime, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), waitingTime, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), condition, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function function which should return an array
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), condition, waitingTime, sleepingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Duration sleepingTime) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), waitingTime, sleepingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), condition, waitingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), waitingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)), condition);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function function which should return an array
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function) {
        ToGetConditionalHelper.checkFunction(function);
        return getFromIterable(t -> asList(function.apply(t)));
    }
}
