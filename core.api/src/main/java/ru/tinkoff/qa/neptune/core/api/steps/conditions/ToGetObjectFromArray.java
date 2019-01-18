package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.*;
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
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
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
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)), checkCondition(condition), waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
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
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)), waitingTime, sleepingTime, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)), condition, waitingTime, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)), waitingTime, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
     * @param checkConditionInParallel is how array should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)), condition, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)), checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
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
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)),
                condition, waitingTime, sleepingTime, checkConditionInParallel, ignoreExceptionOnConditionCheck);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
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
        return getFromIterable(description, t -> asList(function.apply(t)), waitingTime, sleepingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
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
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)),
                condition, waitingTime, checkConditionInParallel, ignoreExceptionOnConditionCheck);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
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
        return getFromIterable(description, t -> asList(function.apply(t)), waitingTime);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param condition predicate which is used to find some target value
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
                                                     Predicate<? super R> condition,
                                                     boolean checkConditionInParallel,
                                                     boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)), condition, checkConditionInParallel,
                ignoreExceptionOnConditionCheck);

    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param description of a value which should be returned.
     * @param function function which should return an array
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    public static <T, R> Function<T, R> getFromArray(String description,
                                                     Function<T, R[]> function) {
        checkFunction(function);
        return getFromIterable(description, t -> asList(function.apply(t)));
    }
}
