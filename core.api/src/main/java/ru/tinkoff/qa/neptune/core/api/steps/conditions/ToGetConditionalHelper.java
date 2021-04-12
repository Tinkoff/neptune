package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofMillis;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

/**
 * This is the util which helps to crate function with given conditions.
 */
@SuppressWarnings("unchecked")
final class ToGetConditionalHelper {

    static final Predicate<?> AS_IS = t -> true;
    private static final Predicate<?> NON_NULL = Objects::nonNull;

    private ToGetConditionalHelper() {
        super();
    }

    static <T> Predicate<T> checkCondition(Predicate<T> condition) {
        checkArgument(nonNull(condition), "Predicate is not defined.");
        return condition;
    }

    static <T, R> Function<T, R> checkFunction(Function<T, R> function) {
        checkArgument(nonNull(function), "Function is not defined.");
        return function;
    }

    static Duration checkSleepingTime(Duration duration) {
        checkArgument(nonNull(duration), "Time of the sleeping is not defined");
        checkArgument(!duration.isNegative(), "Time of the sleeping should be positive");
        return duration;
    }

    static Duration checkWaitingTime(Duration duration) {
        checkArgument(nonNull(duration), "Time of the waiting for some " +
                "valuable result is not defined");
        checkArgument(!duration.isNegative(), "Time of the waiting for some " +
                "valuable should be positive");
        return duration;
    }

    static <T> Predicate<T> notNullAnd(Predicate<? super T> condition) {
        return ((Predicate<T>) NON_NULL).and(condition);
    }

    static boolean printErrorAndFalse(Throwable t) {
        System.err.println(t.getMessage());
        return false;
    }

    static Supplier<? extends RuntimeException> checkExceptionSupplier(Supplier<? extends RuntimeException> exceptionSupplier) {
        checkArgument(nonNull(exceptionSupplier), "Supplier of an exception to be thrown is not defined");
        return exceptionSupplier;
    }

    private static boolean toBeIgnored(Throwable throwable, Collection<Class<? extends Throwable>> toIgnore) {
        var cls = throwable.getClass();
        return toIgnore
                .stream()
                .anyMatch(aClass -> aClass.isAssignableFrom(cls));
    }

    static <T, F> Function<T, F> fluentWaitFunction(Function<T, F> originalFunction,
                                                    @Nullable Duration waitingTime,
                                                    @Nullable Duration sleepingTime,
                                                    Predicate<F> till,
                                                    @Nullable Supplier<? extends RuntimeException> exceptionOnTimeOut,
                                                    Collection<Class<? extends Throwable>> toIgnore) {
        var timeOut = ofNullable(waitingTime).orElseGet(() -> ofMillis(0));
        var sleeping = ofNullable(sleepingTime).orElseGet(() -> ofMillis(50));

        return t -> {
            var currentMillis = currentTimeMillis();
            var endMillis = currentMillis + timeOut.toMillis() + 100;
            F f = null;
            var suitable = false;
            while (currentTimeMillis() < endMillis && !(suitable)) {
                try {
                    f = originalFunction.apply(t);
                } catch (Throwable throwable) {
                    if (toBeIgnored(throwable, toIgnore)) {
                        f = null;
                    } else {
                        throw throwable;
                    }
                }

                suitable = till.test(f);
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
                throw exceptionSupplier1.get();
            }).orElse(f);
        };
    }
}
