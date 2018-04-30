package com.github.toy.constructor.core.api;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

interface DescribedFunction<T, R> extends Function<T, R> {

    String DELIMITER = "->\n";

    private static String tabs(String description) {
        int delimiters = description.split(DELIMITER).length;
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= delimiters; i++) {
            result.append(" ");
        }
        return result.toString();
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

        return new DescribedFunction<>() {
            @Override
            public R apply(T t) {
                V result = before.apply(t);
                return ofNullable(result).map(after).orElse(null);
            }

            @Override
            public String toString() {
                return format("%s ->\n%s%s", before, tabs(before.toString()), after);
            }

            public  <Q> Function<Q, R> compose(Function<? super Q, ? extends T> before) {
                return getSequentialDescribedFunction(before, this);
            }

            public  <U> Function<T, U> andThen(Function<? super R, ? extends U> after) {
                return getSequentialDescribedFunction(this, after);
            }
        };
    }

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return getSequentialDescribedFunction(before, this);
    }

    default  <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return getSequentialDescribedFunction(this, after);
    }
}
