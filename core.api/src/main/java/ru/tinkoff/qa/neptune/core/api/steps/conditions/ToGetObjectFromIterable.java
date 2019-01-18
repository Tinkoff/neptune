package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import ru.tinkoff.qa.neptune.core.api.steps.AsIsCondition;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;

public final class ToGetObjectFromIterable {

    private ToGetObjectFromIterable() {
        super();
    }

    private static <T, R, V extends Iterable<R>> Function<T, R> singleFromIterable(String description,
                                                                                   Function<T, V> function,
                                                                                   Predicate<? super R> condition,
                                                                                   @Nullable Duration waitingTime,
                                                                                   @Nullable Duration sleepingTime,
                                                                                   boolean checkConditionInParallel,
                                                                                   boolean ignoreExceptionOnConditionCheck,
                                                                                   @Nullable Supplier<? extends RuntimeException> exceptionSupplier) {
        return fluentWaitFunction(getDescription(checkDescription(description), condition), t ->
                        ofNullable(function.apply(t))
                                .map(v -> stream(v.spliterator(), checkConditionInParallel).filter(r -> {
                                    try {
                                        return notNullAnd(condition).test(r);
                                    } catch (Throwable t1) {
                                        return returnFalseOrThrowException(t1, ignoreExceptionOnConditionCheck);
                                    }
                                }).findFirst().orElse(null))
                                .orElse(null),
                waitingTime, sleepingTime, Objects::nonNull, exceptionSupplier);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier) {
        return singleFromIterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime), checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier) {

        return singleFromIterable(description, checkFunction(function), AsIsCondition.AS_IS, checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime), false, true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<? super  R> condition,
                                                                               Duration waitingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier) {
        return singleFromIterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime),
                null, checkConditionInParallel, ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier) {
        return singleFromIterable(description, checkFunction(function), AsIsCondition.AS_IS, checkWaitingTime(waitingTime), null, false,
                true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. Some exception is thrown if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier) {
        return singleFromIterable(description, checkFunction(function), checkCondition(condition), null, null, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param exceptionSupplier is a supplier which returns the exception to be thrown on the waiting time
     *                           expiration
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * Some exception is thrown if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Supplier<? extends RuntimeException> exceptionSupplier) {
        return singleFromIterable(description, checkFunction(function), AsIsCondition.AS_IS, null, null,
                false, true, checkExceptionSupplier(exceptionSupplier));
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        return singleFromIterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime),
                checkSleepingTime(sleepingTime), checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param sleepingTime is a duration of the sleeping between attempts to get
     *                     expected valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime,
                                                                               Duration sleepingTime) {
        return singleFromIterable(description, checkFunction(function), AsIsCondition.AS_IS, checkWaitingTime(waitingTime), checkSleepingTime(sleepingTime),
                false, true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               Duration waitingTime,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        return singleFromIterable(description, checkFunction(function), checkCondition(condition), checkWaitingTime(waitingTime),
                null, checkConditionInParallel, ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param waitingTime is a duration of the waiting for valuable result
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Duration waitingTime) {
        return singleFromIterable(description, checkFunction(function), AsIsCondition.AS_IS, checkWaitingTime(waitingTime), null, false,
                true, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value which
     * suits criteria from {@link Iterable}.
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
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found value from {@link Iterable}.
     * It returns a value if something that suits criteria is found. {@code null} is returned if
     * result iterable to get value from is null or has zero-size or it has no item which suits criteria.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function,
                                                                               Predicate<? super R> condition,
                                                                               boolean checkConditionInParallel,
                                                                               boolean ignoreExceptionOnConditionCheck) {
        return singleFromIterable(checkDescription(description), checkFunction(function), checkCondition(condition), null, null, checkConditionInParallel,
                ignoreExceptionOnConditionCheck, null);
    }

    /**
     * This method returns a function. The result function returns a single first found value from {@link Iterable}.
     *
     * @param description of a value which should be returned
     * @param function function which should return {@link Iterable}
     * @param <T> is a type of input value
     * @param <R> is a type of the target value
     * @param <V> is a type of {@link Iterable} of {@code R}
     * @return a function. The result function returns a single first found non-null value from {@link Iterable}.
     * {@code null} is returned if result iterable to get value from is null or has zero-size.
     */
    public static <T, R, V extends Iterable<R>> Function<T, R> getFromIterable(String description,
                                                                               Function<T, V> function) {
        return singleFromIterable(description, checkFunction(function), AsIsCondition.AS_IS, null, null, false,
                true, null);
    }
}
