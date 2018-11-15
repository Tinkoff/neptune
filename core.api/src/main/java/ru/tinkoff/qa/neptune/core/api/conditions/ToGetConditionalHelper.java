package ru.tinkoff.qa.neptune.core.api.conditions;

import ru.tinkoff.qa.neptune.core.api.AsIsCondition;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Objects;
import java.util.function.*;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofMillis;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.utils.IsDescribedUtil.isDescribed;

/**
 * This is the util which helps to crate function with given conditions.
 */
@SuppressWarnings("unchecked")
final class ToGetConditionalHelper {

    private ToGetConditionalHelper() {
        super();
    }

    static <T> Predicate<T> checkCondition(Predicate<T> condition) {
        checkArgument(condition != null, "Predicate is not defined.");
        checkArgument(isDescribed(condition),
                "Condition is not described. " +
                        "Use StoryWriter.conditions to describe it.");
        return condition;
    }

    static String checkDescription(String description) {
        checkArgument(!isBlank(description), "Description should not be empty or null value");
        return description;
    }

    static <T, R>  Function<T, R> checkFunction(Function<T, R> function) {
        checkArgument(function != null, "Function is not defined.");
        return function;
    }

    static Duration checkSleepingTime(Duration duration) {
        checkArgument(duration != null, "Time of the sleeping is not defined");
        checkArgument(!duration.isNegative(), "Time of the sleeping should be positive");
        return duration;
    }

    static Duration checkWaitingTime(Duration duration) {
        checkArgument(duration != null, "Time of the waiting for some " +
                "valuable result is not defined");
        checkArgument(!duration.isNegative(), "Time of the waiting for some " +
                "valuable should be positive");
        return duration;
    }

    static <T> Predicate<T> notNullAnd(Predicate<? super T> condition) {
        return ((Predicate<T>) condition("is not null value", Objects::nonNull))
                .and(condition);
    }

    static boolean returnFalseOrThrowException(Throwable t, boolean ignoreExceptionOnConditionCheck) {
        var message = format("%s was caught. Message: %s", t.getClass().getName(), t.getMessage());
        if (!ignoreExceptionOnConditionCheck) {
            throw new CheckConditionException(message, t);
        }

        System.err.println(message);
        return false;
    }

    static Supplier<? extends RuntimeException> checkExceptionSupplier(Supplier<? extends RuntimeException> exceptionSupplier) {
        checkArgument(exceptionSupplier != null,
                "Supplier of an exception to be thrown is not defined");
        return exceptionSupplier;
    }

    static String getDescription(String description, Predicate<?> condition) {
        var resultDescription = description;

        if (!AsIsCondition.AS_IS.equals(condition)) {
            resultDescription = format("%s. Criteria: %s", resultDescription, condition).trim();
        }

        return resultDescription;
    }

    static <T, F> Function<T, F> fluentWaitFunction(String description,
                                                    Function<T, F> originalFunction,
                                                    @Nullable Duration waitingTime,
                                                    @Nullable Duration sleepingTime,
                                                    Predicate<F> till,
                                                    @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut) {
        var timeOut = ofNullable(waitingTime).orElseGet(() -> ofMillis(0));
        var sleeping = ofNullable(sleepingTime).orElseGet(() -> ofMillis(50));

        return toGet(description, t -> {
            var currentMillis = currentTimeMillis();
            var endMillis = currentMillis + timeOut.toMillis() + 100;
            F f = null;
            var suitable = false;
            while (currentTimeMillis() < endMillis && !(suitable)) {
                suitable = till.test(f = originalFunction.apply(t));
                try {
                    sleep(sleeping.toMillis());
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }

            if (suitable) {
                return f;
            }

            return (F) ofNullable(exceptionOnTimeOut).map(exceptionSupplier1 -> {
                throw exceptionOnTimeOut.get();
            }).orElse(f);
        });
    }


    private static class CheckConditionException extends RuntimeException {
        CheckConditionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
