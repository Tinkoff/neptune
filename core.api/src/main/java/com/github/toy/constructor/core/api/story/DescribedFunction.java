package com.github.toy.constructor.core.api.story;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface DescribedFunction<T, R> extends Function<T, R> {

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        requireNonNull(before);
        checkArgument(DescribedFunction.class.isAssignableFrom(before.getClass()),
                "It seems given function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get previously.");

        Function<T, R> after = this;
        return new DescribedFunction<>() {
            @Override
            public R apply(V v) {
                T result = before.apply(v);
                return ofNullable(result).map(after).orElse(null);
            }

            @Override
            public String toString() {
                return format("%s from (%s)", after.toString(), before.toString());
            }
        };
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        requireNonNull(after);
        checkArgument(DescribedFunction.class.isAssignableFrom(after.getClass()),
                "It seems given function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe the value to get after.");

        Function<T, R> before = this;
        return new DescribedFunction<>() {
            @Override
            public V apply(T t) {
                R result = before.apply(t);
                return ofNullable(result).map(after).orElse(null);
            }

            @Override
            public String toString() {
                return format("%s from (%s)", after.toString(), before.toString());
            }
        };
    }
}
