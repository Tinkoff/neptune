package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;
import ru.tinkoff.qa.neptune.core.api.exception.management.StopsIgnoreThrowable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

class StepFunction<T, R> implements Function<T, R>, IgnoresThrowable<StepFunction<T, R>>,
        StopsIgnoreThrowable<StepFunction<T, R>> {

    private final String description;
    private final Function<T, R> function;
    final Set<Class<? extends Throwable>> ignored = new HashSet<>();

    StepFunction(String description, Function<T, R> function) {
        checkArgument(nonNull(function), "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty string or null value");
        this.description = description;
        this.function = function;
    }

    private boolean shouldBeThrowableIgnored(Throwable toBeIgnored) {
        for (var throwableClass: ignored) {
            if (throwableClass.isAssignableFrom(toBeIgnored.getClass())) {
                return true;
            }
        }
        return false;
    }

    private static <R> void fireReturnedValueIfNecessary(R r, boolean isComplex) {
        if (!isComplex) {
            if (isLoggable(r)) {
                fireReturnedValue(r);
            }
        }
    }

    private void fireEventStartingIfNecessary(T t, boolean isComplex) {
        if (isComplex) {
            return;
        }

        if (isLoggable(t)) {
            fireEventStarting(format("From %s get %s", t, description));
        }
        else {
            fireEventStarting(format("Get %s", description));
        }
    }

    private static void fireThrownExceptionIfNecessary(Throwable thrown, boolean isComplex) {
        if (!isComplex) {
            fireThrownException(thrown);
        }
    }

    private void fireEventFinishingIfNecessary(boolean isComplex) {
        if (!isComplex) {
            fireEventFinishing();
        }
    }

    @Override
    public R apply(T t) {
        var isComplex = SequentialStepFunction.class.equals(this.getClass());
        try {
            fireEventStartingIfNecessary(t, isComplex);
            R result = function.apply(t);
            fireReturnedValueIfNecessary(result, isComplex);
            if (catchSuccessEvent() && !isComplex && !StepFunction.class.isAssignableFrom(function.getClass())) {
                catchValue(result, format("Getting of '%s' succeed. The result", description));
                catchValue(t, "Value that was used to get a result from");
            }
            return result;
        }
        catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                fireThrownExceptionIfNecessary(thrown, isComplex);
                if (catchFailureEvent() && !isComplex && !StepFunction.class.isAssignableFrom(function.getClass())) {
                    catchValue(t, format("Getting of '%s' failed. Value that was used to get a result from", description));
                }
                throw thrown;
            }
            else {
                fireReturnedValueIfNecessary(null, isComplex);
                return null;
            }
        }
        finally {
            fireEventFinishingIfNecessary(isComplex);
        }
    }

    @Override
    public String toString() {
        return description;
    }

    public <V> StepFunction<V, R> compose(Function<? super V, ? extends T> before) {
        return new SequentialStepFunction<>(before, this);
    }

    public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        return new SequentialStepFunction<>(this, after);
    }

    @Override
    public StepFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return this;
    }

    @Override
    public StepFunction<T, R> stopIgnore(List<Class<? extends Throwable>> toStopIgnore) {
        ignored.removeAll(toStopIgnore);
        return this;
    }

    Set<Class<? extends Throwable>> getIgnored() {
        return new HashSet<>(ignored);
    }

    private static class SequentialStepFunction<T, R> extends StepFunction<T, R> {

        private final Function<? super T, ?> before;
        private final Function<?, ? extends R> after;

        private <V> SequentialStepFunction(Function<? super T, ? extends V> before,
                                           Function<? super V, ? extends R> after) {
            super(after.toString(), getChainOfFunctions(before, after));
            this.before = before;
            this.after = after;
        }

        private static <T, V, R> Function<T, R> getChainOfFunctions(Function<? super T, ? extends V> before,
                                                                    Function<? super V, ? extends R> after) {
            checkNotNull(before);
            checkNotNull(after);
            checkArgument(StepFunction.class.isAssignableFrom(after.getClass()),
                    "It seems given after-function doesn't describe any value to get. Use method " +
                            "StoryWriter.toGet to describe the value to get");
            return t -> {
                V result = before.apply(t);
                return ofNullable(result).map(after).orElse(null);
            };
        }

        @Override
        public StepFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
            super.addIgnored(toBeIgnored);
            var ignored = new ArrayList<>(this.ignored);
            if (IgnoresThrowable.class.isAssignableFrom(before.getClass())) {
                ((IgnoresThrowable) before).addIgnored(ignored);
            }

            ((IgnoresThrowable) after).addIgnored(ignored);
            return this;
        }

        @Override
        public StepFunction<T, R> stopIgnore(List<Class<? extends Throwable>> toStopIgnore) {
            super.stopIgnore(toStopIgnore);
            var notIgnored = new ArrayList<>(this.ignored);
            if (StopsIgnoreThrowable.class.isAssignableFrom(before.getClass())) {
                ((StopsIgnoreThrowable) before).stopIgnore(notIgnored);
            }

            ((StopsIgnoreThrowable) after).stopIgnore(notIgnored);
            return this;
        }

        public <V> StepFunction<V, R> compose(Function<? super V, ? extends T> before) {
            return new SequentialStepFunction<>(before, this);
        }

        public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
            return new SequentialStepFunction<>(this, after);
        }
    }
}
