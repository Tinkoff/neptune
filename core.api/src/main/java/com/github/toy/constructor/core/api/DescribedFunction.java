package com.github.toy.constructor.core.api;

import com.github.toy.constructor.core.api.exception.management.IgnoresThrowable;
import com.github.toy.constructor.core.api.exception.management.StopsIgnoreThrowable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StaticEventFiring.*;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchFailureEvent;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.catchSuccessEvent;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
class DescribedFunction<T, R> implements Function<T, R>, IgnoresThrowable<DescribedFunction<T, R>>,
        StopsIgnoreThrowable<DescribedFunction<T, R>> {

    private final String description;
    private final Function<T, R> function;
    private boolean isComplex;
    protected final Set<Class<? extends Throwable>> ignored = new HashSet<>();

    DescribedFunction(String description, Function<T, R> function) {
        checkArgument(function != null, "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.function = function;
    }

    private static <T, V, R> Function<T, R> getSequentialDescribedFunction(Function<? super T, ? extends V> before,
                                                                           Function<? super V, ? extends R> after) {
        checkNotNull(before);
        checkNotNull(after);
        checkArgument(DescribedFunction.class.isAssignableFrom(after.getClass()),
                "It seems given after-function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get previously.");


        return new DescribedFunction<T, R>(after.toString(), t -> {
            V result = before.apply(t);
            return ofNullable(result).map(after).orElse(null);
        }).setComplex();
    }

    private boolean shouldBeThrowableIgnored(Throwable toBeIgnored) {
        for (Class<? extends Throwable> throwableClass: ignored) {
            if (throwableClass.isAssignableFrom(toBeIgnored.getClass())) {
                return true;
            }
        }
        return false;
    }

    private void fireEventStartingIfNecessary(T t) {
        if (isComplex) {
            return;
        }

        Class<?> valueClass = t.getClass();
        if (!GetStep.class.isAssignableFrom(valueClass) &&
                !PerformStep.class.isAssignableFrom(valueClass)) {
            fireEventStarting(format("From %s get %s", t, description));
        }
        else {
            fireEventStarting(format("Get %s", description));
        }
    }

    private void fireReturnedValueIfNecessary(R r) {
        if (!isComplex) {
            fireReturnedValue(r);
        }
    }

    private void fireThrownExceptionIfNecessary(Throwable thrown) {
        if (!isComplex) {
            fireThrownException(thrown);
        }
    }

    private void fireEventFinishingIfNecessary() {
        if (!isComplex) {
            fireEventFinishing();
        }
    }

    @Override
    public R apply(T t) {
        try {
            fireEventStartingIfNecessary(t);
            R result = function.apply(t);
            fireReturnedValueIfNecessary(result);
            if (catchSuccessEvent() && !isComplex) {
                catchResult(result, format("Getting of '%s' succeed", description));
            }
            return result;
        }
        catch (Throwable thrown) {
            if (!shouldBeThrowableIgnored(thrown)) {
                fireThrownExceptionIfNecessary(thrown);
                if (catchFailureEvent() && !isComplex) {
                    catchResult(t, format("Getting of '%s' failed", description));
                }
                throw thrown;
            }
            else {
                fireReturnedValueIfNecessary(null);
                return null;
            }
        }
        finally {
            fireEventFinishingIfNecessary();
        }
    }

    @Override
    public String toString() {
        return description;
    }

    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return getSequentialDescribedFunction(before, this);
    }

    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return getSequentialDescribedFunction(this, after);
    }

    private DescribedFunction<T, R> setComplex() {
        isComplex = true;
        return this;
    }

    @Override
    public DescribedFunction<T, R> addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return this;
    }

    @Override
    public DescribedFunction<T, R> stopIgnore(List<Class<? extends Throwable>> toStopIgnore) {
        ignored.removeAll(toStopIgnore);
        return this;
    }
}
