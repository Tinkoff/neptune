package com.github.toy.constructor.core.api;

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
class DescribedFunction<T, R> implements Function<T, R> {

    private final String description;
    private final Function<T, R> function;
    private boolean isComplex;

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
        checkArgument(DescribedFunction.class.isAssignableFrom(before.getClass()),
                "It seems given before-function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get previously.");
        checkArgument(DescribedFunction.class.isAssignableFrom(after.getClass()),
                "It seems given after-function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get previously.");


        return new DescribedFunction<T, R>(after.toString(), t -> {
            V result = before.apply(t);
            return ofNullable(result).map(after).orElse(null);
        }).setComplex();
    }

    @Override
    public R apply(T t) {
        try {
            fireEventStarting(format("From %s get %s", t, description));
            R result = function.apply(t);
            fireReturnedValue(result);
            if (catchSuccessEvent() && !isComplex) {
                catchResult(result, format("Getting of '%s' succeed", description));
            }
            return result;
        }
        catch (Throwable thrown) {
            fireThrownException(thrown);
            if (catchFailureEvent() && !isComplex) {
                catchResult(t, format("Getting of '%s' failed", description));
            }
            throw thrown;
        }
        finally {
            fireEventFinishing();
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
}
