package com.github.toy.constructor.core.api;

import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.ToGetConditionalHelper.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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
                                                                         @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        return ToGetConditionalHelper.fluentWaitFunction(ToGetConditionalHelper.getDescription(description, function, condition), t ->
                        ofNullable(function.apply(t)).map(v -> {
                            List<R> result = stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                                try {
                                    return !ToGetConditionalHelper.notNullAnd(condition).test(r);
                                } catch (Throwable t1) {
                                    return !ToGetConditionalHelper.returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                }
                            }).collect(toList());

                            Iterables.removeAll(v, result);
                            return v;
                        }).orElse(null),
                waitingTime, sleepingTime, v -> v != null && Iterables.size(v) > 0, exceptionOnTimeOut);

    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<? super R> condition,
                                                                              Duration waitingTime,
                                                                              Duration sleepingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck,
                                                                              Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
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
                                                                           Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, sleepingTime,
                true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param waitingTime is a duration of the waiting for valuable result
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<? super R> condition,
                                                                              Duration waitingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck,
                                                                              Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
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
                                                                           Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkWaitingTime(waitingTime);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, null,
                true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
     * @param checkConditionInParallel is how iterable should be matched. If {@code true} when each value will be
     *                                 checked in parallel.
     * @param ignoreExceptionOnConditionCheck is used to define what should be done when check is failed
     *                                        and some exception is thrown. Exception will be thrown when
     *                                        {@code true}.
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria. It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements which suit the criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<? super R> condition,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck,
                                                                              Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        checkCondition(condition);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
     * @param exceptionOnTimeOut is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of target values
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns an {@link Iterable} of elements which differ from null.
     * It returns not empty iterable when there are such elements. Some exception is thrown if result
     * iterable is null or has no elements or all elements are {@code null}.
     */
    public static <T, R, V extends Iterable<R>> Function<T, V> getIterable(Function<T, V> function,
                                                                           Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        checkFunction(function);
        ToGetConditionalHelper.checkExceptionSupplier(exceptionOnTimeOut);
        return iterable(EMPTY, function, ToGetConditionalHelper.asIs(), null, null,
                true, true, exceptionOnTimeOut);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
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
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<? super R> condition,
                                                                              Duration waitingTime,
                                                                              Duration sleepingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return iterable(description, function, condition, waitingTime, sleepingTime,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
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
        checkFunction(function);
        checkWaitingTime(waitingTime);
        checkSleepingTime(sleepingTime);
        return iterable(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, sleepingTime,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
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
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<? super R> condition,
                                                                              Duration waitingTime,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        checkWaitingTime(waitingTime);
        return iterable(description, function, condition, waitingTime, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null.
     *
     * @param function described function which should return {@link Iterable}
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
        checkFunction(function);
        checkWaitingTime(waitingTime);
        return iterable(EMPTY, function, ToGetConditionalHelper.asIs(), waitingTime, null,
                true, true, null);
    }

    /**
     * This method returns a function. The result function returns an {@link Iterable} of elements which differ from null
     * and suit the criteria.
     *
     * @param description of a value which should be returned.
     * @param function described function which should return {@link Iterable}
     * @param condition described predicate which is used to find some target value
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
    public static <T, R, V extends Iterable<R>> Function<T, V> getSubIterable(String description,
                                                                              Function<T, V> function,
                                                                              Predicate<? super R> condition,
                                                                              boolean checkConditionInParallel,
                                                                              boolean ignoreExceptionOnConditionCheck) {
        checkFunction(function);
        checkCondition(condition);
        return iterable(description, function, condition, null, null,
                checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }
}
