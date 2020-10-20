package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.*;

@SuppressWarnings("unchecked")
public final class ToGetObjectFromIterable {

    private ToGetObjectFromIterable() {
        super();
    }

    private static <T, R, V extends Iterable<R>> Function<T, R> singleFromIterable(Function<T, V> function,
                                                                                   Predicate<? super R> condition,
                                                                                   @Nullable Duration waitingTime,
                                                                                   @Nullable Duration sleepingTime,
                                                                                   @Nullable Supplier<? extends RuntimeException> exceptionSupplier,
                                                                                   Collection<Class<? extends Throwable>> toIgnore) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t))
                                .map(rs -> {
                                    for (var r : rs) {
                                        try {
                                            if (notNullAnd(condition).test(r)) {
                                                return r;
                                            }
                                        } catch (Throwable t1) {
                                            printErrorAndFalse(t1);
                                        }
                                    }
                                    return null;
                                })
                                .orElse(null),
                waitingTime, sleepingTime, Objects::nonNull, exceptionSupplier, toIgnore);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
     *
     * @param function          function which should return {@link Iterable}
     * @param condition         predicate which is used to find some target value
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param function          function which should return {@link Iterable}
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param sleepingTime      is a duration of the sleeping between attempts to get
     *                          expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                                                               Class<? extends Throwable>... toIgnore) {

        return singleFromIterable(checkFunction(function),
                (Predicate<? super R>) AS_IS, checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
     *
     * @param function          function which should return {@link Iterable}
     * @param condition         predicate which is used to find some target value
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Duration waitingTime,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param function          function which should return {@link Iterable}
     * @param waitingTime       is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
     *
     * @param function          function which should return {@link Iterable}
     * @param condition         predicate which is used to find some target value
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                checkCondition(condition),
                null, null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param function          function which should return {@link Iterable}
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                          expiration
     * @param toIgnore          classes of exception to be ignored during execution
     * @param <T>               is a type of input value
     * @param <R>               is a type of the target value
     * @param <V>               is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                null, null,
                checkExceptionSupplier(exceptionSupplier),
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
     *
     * @param function     function which should return {@link Iterable}
     * @param condition    predicate which is used to find some target value
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore     classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of the target value
     * @param <V>          is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param function     function which should return {@link Iterable}
     * @param waitingTime  is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param toIgnore     classes of exception to be ignored during execution
     * @param <T>          is a type of input value
     * @param <R>          is a type of the target value
     * @param <V>          is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime),
                null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
     *
     * @param function    function which should return {@link Iterable}
     * @param condition   predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore    classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of the target value
     * @param <V>         is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Duration waitingTime,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                checkCondition(condition),
                checkWaitingTime(waitingTime),
                null, null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param function    function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param toIgnore    classes of exception to be ignored during execution
     * @param <T>         is a type of input value
     * @param <R>         is a type of the target value
     * @param <V>         is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                checkWaitingTime(waitingTime),
                null, null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
     *
     * @param function  function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param toIgnore  classes of exception to be ignored during execution
     * @param <T>       is a type of input value
     * @param <R>       is a type of the target value
     * @param <V>       is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                checkCondition(condition),
                null,
                null, null,
                asList(toIgnore));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param function function which should return {@link Iterable}
     * @param toIgnore classes of exception to be ignored during execution
     * @param <T>      is a type of input value
     * @param <R>      is a type of the target value
     * @param <V>      is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(Function<T, V> function,
                                                                               Class<? extends Throwable>... toIgnore) {
        return singleFromIterable(checkFunction(function),
                (Predicate<? super R>) AS_IS,
                null, null, null,
                asList(toIgnore));
    }
}
