package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.fluentWaitFunction;

@SuppressWarnings("unchecked")
public final class ToGetSubIterable {

    private ToGetSubIterable() {
        super();
    }
    
    private static <T, R, V extends Iterable<R>> Function<T, V> iterable(Function<T, V> function,
                                                                         Predicate<? super R> condition,
                                                                         @Nullable Duration waitingTime,
                                                                         @Nullable Duration sleepingTime,
                                                                         @Nullable Supplier<? extends RuntimeException> exceptionSupplier) {
        return fluentWaitFunction(t ->
                        ofNullable(function.apply(t)).map(v -> {
                            var result = stream(v.spliterator(), false).filter(r -> {
                                try {
                                    return !ToGetConditionalHelper.notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return !ToGetConditionalHelper.printErrorAndFalse(t1);
                                }
                            }).collect(toList());

                            Iterables.removeAll(v, result);
                            return v;
                        }).orElse(null),
                waitingTime, sleepingTime, v -> nonNull(v) && Iterables.size(v) > 0, exceptionSupplier);

    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime), ToGetConditionalHelper.checkSleepingTime(sleepingTime),
                ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime),
                ToGetConditionalHelper.checkSleepingTime(sleepingTime), ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime), null,
                ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime), null,
                ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), null, null,
                ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function function which should return {@link Iterable}
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, null, null,
                ToGetConditionalHelper.checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime) {
        return iterable(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime),
                ToGetConditionalHelper.checkSleepingTime(sleepingTime), null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime) {
        return iterable(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime), ToGetConditionalHelper.checkSleepingTime(sleepingTime),
                null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime) {
        return iterable(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), ToGetConditionalHelper.checkWaitingTime(waitingTime), null,
                null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Duration waitingTime) {
        return iterable(ToGetConditionalHelper.checkFunction(function), (Predicate<? super R>) ToGetConditionalHelper.AS_IS, ToGetConditionalHelper.checkWaitingTime(waitingTime), null,
                null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Predicate<? super R> condition) {
        return iterable(ToGetConditionalHelper.checkFunction(function), ToGetConditionalHelper.checkCondition(condition), null, null,  null);
    }
}
