package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import org.apache.commons.lang3.ArrayUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.*;

@SuppressWarnings("unchecked")
public final class ToGetSubArray {

    private ToGetSubArray() {
        super();
    }

    private static <T, R> Function<T, R[]> array(Function<T, R[]> function,
                                                 Predicate<? super R> condition,
                                                 Duration waitingTime,
                                                 Duration sleepingTime,
                                                 Supplier<? extends RuntimeException> exceptionSupplier,
                                                 Collection<Class<? extends Throwable>> toIgnore) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t)).map(rs -> {
                            var subResult = Arrays.stream(rs).filter(r -> {
                                try {
                                    return !notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return !printErrorAndFalse(t1);
                                }
                            }).collect(toList());

                            R[] result = rs;
                            for (R r : subResult) {
                                result = ArrayUtils.removeElement(result, r);
                            }
                            return result;
                        }).orElse(null),
                waitingTime, sleepingTime, rs -> nonNull(rs) && rs.length > 0, exceptionSupplier, toIgnore);
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
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
     * @param <R>               is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function          function which should return an array
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param function          function which should return an array
     * @param condition         predicate which is used to find some target value
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function          function which should return an array
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param function          function which should return an array
     * @param condition         predicate which is used to find some target value
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                checkCondition(condition),
                null,
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function          function which should return an array
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Some exception is thrown if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                null,
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param function     function which should return {@link Iterable}
     * @param condition    predicate which is used to find some target value
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore     classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function     function which should return {@link Iterable}
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore     classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Duration sleepingTime,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null
     * and suit the criteria.
     *
     * @param function    function which should return {@link Iterable}
     * @param condition   predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore    classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of target values
     * @return a function. The result function returns an array of elements which differ from null
     * and suit the criteria. It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements which suit the criteria.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Duration waitingTime,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                null,
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns an array of elements which differ from null.
     *
     * @param function    function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore    classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of target values
     * @return a function. The result function returns an array of elements which differ from null.
     * It returns not empty array when there are such elements. Empty array is returned if result
     * array is null or has no elements or all elements are {@code null}.
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Duration waitingTime,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                null,
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns sub-array of found values from array.
     * The original function should return array to match.
     *
     * @param function  which should return an array.
     * @param condition which is used to find target values.
     * @param toIgnore  classes of exception to be ignored during execution
     * @param <T>       is a type of input value
     * @param <R>       is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Empty array or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Predicate<? super R> condition,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                checkCondition(condition),
                null,
                null,
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function.The result function returns an array of elements which differ from {@code null.}
     *
     * @param function which should return an array.
     * @param toIgnore classes of exception to be ignored during execution
     * @param <T>      is a type of input value
     * @param <R>      is a type of target values
     * @return a function. The result function returns sub-array of found values from array.
     * The result function will return values if something is found. Empty array or {@code null} are
     * returned otherwise. It depends on result of the {@link Function#apply(Object)}
     */
    public static <T, R> Function<T, R[]> getArray(Function<T, R[]> function,
                                                   Class<? extends Throwable>... toIgnore) {
        return array(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                null,
                null,
                null,
                asList(toIgnore));
    }
}
