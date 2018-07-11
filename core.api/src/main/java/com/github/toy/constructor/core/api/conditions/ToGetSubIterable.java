package com.github.toy.constructor.core.api.conditions;

import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.AsIsCondition.AS_IS;
import static com.github.toy.constructor.core.api.conditions.ToGetConditionalHelper.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public final class ToGetSubIterable {

    private ToGetSubIterable() {
        super();
    }
    
    private static <T, R, V extends Iterable<R>> Function<T, V> iterable(String description,
                                                                         Function<T, V> function,
                                                                         Predicate<? super R> condition,
                                                                         @Nullable Duration waitingTime,
                                                                         @Nullable Duration sleepingTime,
                                                                         boolean checkConditionInParallel,
                                                                         boolean ignoreExceptionOnConditionCheck,
                                                                         @Nullable Supplier<? extends RuntimeException> exceptionSupplier) {
        return fluentWaitFunction(getDescription(checkDescription(description), condition), t ->
                        ofNullable(function.apply(t)).map(v -> {
                            List<R> result = stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                                try {
                                    return !notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return !returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                }
                            }).collect(toList());

                            Iterables.removeAll(v, result);
                            return v;
                        }).orElse(null),
                waitingTime, sleepingTime, v -> v != null && Iterables.size(v) > 0, exceptionSupplier);

    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime,
                                                                           boolean checkConditionInParallel,
                                                                           boolean ignoreExceptionOnConditionCheck,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime), checkSleepingTime(sleepingTime),
                checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param description of a value which should be returned
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
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(description, checkFunction(function), AS_IS, checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime), true, true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime,
                                                                           boolean checkConditionInParallel,
                                                                           boolean ignoreExceptionOnConditionCheck,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime), null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param description of a value which should be returned
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
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(description, checkFunction(function), AS_IS, checkWaitingTime(waitingTime), null, true,
                true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           boolean checkConditionInParallel,
                                                                           boolean ignoreExceptionOnConditionCheck,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(description, checkFunction(function), checkCondition(condition), null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param description of a value which should be returned
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
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Supplier<? extends RuntimeException> exceptionSupplier) {
        return iterable(description, checkFunction(function), AS_IS, null, null, true,
                true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime,
                                                                           boolean checkConditionInParallel,
                                                                           boolean ignoreExceptionOnConditionCheck) {
        return iterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime), checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param description of a value which should be returned
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
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Duration waitingTime,
                                                                           Duration sleepingTime) {
        return iterable(description, checkFunction(function), AS_IS, checkWaitingTime(waitingTime), checkSleepingTime(sleepingTime),
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           Duration waitingTime,
                                                                           boolean checkConditionInParallel,
                                                                           boolean ignoreExceptionOnConditionCheck) {
        return iterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime), null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Duration waitingTime) {
        return iterable(description, checkFunction(function), AS_IS, checkWaitingTime(waitingTime), null,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param condition predicate which is used to find some target value
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Empty iterable is returned if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(String description,
                                                                           Function<T, V> function,
                                                                           Predicate<? super R> condition,
                                                                           boolean checkConditionInParallel,
                                                                           boolean ignoreExceptionOnConditionCheck) {
        return iterable(description,
                checkFunction(function), checkCondition(condition), null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }
}
