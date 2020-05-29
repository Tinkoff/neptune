package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.*;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromIterable.getFromIterable;

public final class ToGetObjectFromArray {

    private ToGetObjectFromArray() {
        super();
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function          function which should return an array
     * @param condition         predicate which is used to find some target value
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                checkCondition(condition),
                waitingTime,
                sleepingTime,
                exceptionSupplier,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function          function which should return an array
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                waitingTime,
                sleepingTime,
                exceptionSupplier,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function          function which should return an array
     * @param condition         predicate which is used to find some target value
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                condition,
                waitingTime,
                exceptionSupplier,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function          function which should return an array
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Supplier<? extends RuntimeException> exceptionSupplier,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                waitingTime,
                exceptionSupplier,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function          function which should return an array
     * @param condition         predicate which is used to find some target value
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Supplier<? extends RuntimeException> exceptionSupplier,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                condition,
                exceptionSupplier,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function          function which should return an array
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * Some exception is thrown if result array to get value from is null or has zero-length.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Supplier<? extends RuntimeException> exceptionSupplier,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                checkExceptionSupplier(exceptionSupplier),
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function     function which should return an array
     * @param condition    predicate which is used to find some target value
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore     classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                condition,
                waitingTime,
                sleepingTime,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function     function which should return an array
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore     classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Duration sleepingTime,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                waitingTime,
                sleepingTime,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function    function which should return an array
     * @param condition   predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore    classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of the target value
     * @return a function. The result function returns a single first found value from array.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result array to get value from is null or has zero-length or it has no item which suits criteria.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Duration waitingTime,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                condition,
                waitingTime,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function    function which should return an array
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore    classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Duration waitingTime,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                waitingTime,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from array.
     *
     * @param function  function which should return an array
     * @param condition predicate which is used to find some target value
     * @param toIgnore  classes of exception to be ignored during execution
     * @param <T>       is a type of input value
     * @param <R>       is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Predicate<? super R> condition,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                condition,
                toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value from array.
     *
     * @param function function which should return an array
     * @param toIgnore classes of exception to be ignored during execution
     * @param <T>      is a type of input value
     * @param <R>      is a type of the target value
     * @return a function. The result function returns a single first found non-null value from array.
     * {@code null} is returned if result array to get value from is null or has zero-length.
     */
    @SafeVarargs
    public static <T, R> Function<T, R> getFromArray(Function<T, R[]> function,
                                                     Class<? extends Throwable>... toIgnore) {
        checkFunction(function);
        return getFromIterable(t -> {
                    var array = function.apply(t);
                    return ofNullable(array).map(Arrays::asList).orElse(null);
                },
                toIgnore);
    }
}
