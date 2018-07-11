package com.github.toy.constructor.core.api;

import com.github.toy.constructor.core.api.exception.management.IgnoresThrowable;
import com.github.toy.constructor.core.api.exception.management.StopsIgnoreThrowable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.event.firing.StaticEventFiring.*;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchFailureEvent;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.github.toy.constructor.core.api.utils.IsDescribedUtil.isDescribed;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
class StepFunction<T, R> implements Function<T, R>, IgnoresThrowable<StepFunction<T, R>>,
        StopsIgnoreThrowable<StepFunction<T, R>> {

    private final String description;
    private final Function<T, R> function;
    private final Set<Class<? extends Throwable>> ignored = new HashSet<>();
    private List<Function<?, ?>> functions;

    StepFunction(String description, Function<T, R> function) {
        checkArgument(function != null, "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.function = function;
    }

    private static <T, V, R> StepFunction<T, R> getSequentialDescribedFunction(Function<? super T, ? extends V> before,
                                                                           Function<? super V, ? extends R> after) {
        checkNotNull(before);
        checkNotNull(after);
        checkArgument(isDescribed(after),
                "It seems given after-function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe this value or override the toString method");


        StepFunction<T, R> resultFunction = new StepFunction<>(after.toString(), t -> {
            V result = before.apply(t);
            return ofNullable(result).map(after).orElse(null);
        });
        resultFunction.functions = of(before, after);
        return resultFunction;
    }

    private boolean shouldBeThrowableIgnored(Throwable toBeIgnored) {
        for (Class<? extends Throwable> throwableClass: ignored) {
            if (throwableClass.isAssignableFrom(toBeIgnored.getClass())) {
                return true;
            }
        }
        return false;
    }

    private boolean isComplex() {
        return functions != null;
    }

    private void fireEventStartingIfNecessary(T t, boolean isComplex) {
        if (isComplex) {
            return;
        }

        Class<?> valueClass = t.getClass();
        if (!GetStep.class.isAssignableFrom(valueClass) &&
                !PerformActionStep.class.isAssignableFrom(valueClass)) {
            fireEventStarting(format("From %s get %s", t, description));
        }
        else {
            fireEventStarting(format("Get %s", description));
        }
    }

    private static <R> void fireReturnedValueIfNecessary(R r, boolean isComplex) {
        if (!isComplex) {
            fireReturnedValue(r);
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
        boolean isComplex = isComplex();
        try {
            fireEventStartingIfNecessary(t, isComplex);
            R result = function.apply(t);
            fireReturnedValueIfNecessary(result, isComplex);
            if (catchSuccessEvent() && !isComplex && !StepFunction.class.isAssignableFrom(function.getClass())) {
                catchResult(result, format("Getting of '%s' succeed", description));
            }
            return result;
        }
        catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                fireThrownExceptionIfNecessary(thrown, isComplex);
                if (catchFailureEvent() && !isComplex && !StepFunction.class.isAssignableFrom(function.getClass())) {
                    catchResult(t, format("Getting of '%s' failed", description));
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
        return getSequentialDescribedFunction(before, this);
    }

    public <V> StepFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        return getSequentialDescribedFunction(this, after);
    }

    @Override
    public StepFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        ofNullable(functions).ifPresent(functionList -> functionList.forEach(function1 -> {
            if (StepFunction.class.isAssignableFrom(function1.getClass())) {
                StepFunction.class.cast(function1).addIgnored(toBeIgnored);
            }
        }));
        return this;
    }

    @Override
    public StepFunction<T, R> stopIgnore(List<Class<? extends Throwable>> toStopIgnore) {
        ignored.removeAll(toStopIgnore);
        ofNullable(functions).ifPresent(functionList -> functionList.forEach(function1 -> {
            if (StepFunction.class.isAssignableFrom(function1.getClass())) {
                StepFunction.class.cast(function1).stopIgnore(toStopIgnore);
            }
        }));
        return this;
    }

    Set<Class<? extends Throwable>> getIgnored() {
        return new HashSet<>(ignored);
    }
}
