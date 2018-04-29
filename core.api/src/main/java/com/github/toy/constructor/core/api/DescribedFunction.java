package com.github.toy.constructor.core.api;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@SuppressWarnings({"unchecked"})
abstract class DescribedFunction<T, R> implements Function<T, R> {

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
                if (GetStep.class.isAssignableFrom(t.getClass())) {
                    GetStep getStep = GetStep.class.cast(t);
                    Object result = getStep.get(before);
                    V v = (V) result;
                    return (R) getStep.get(toGet(format("From %s get %s", valueOf(v), after),
                            step1 -> ofNullable(v).map(after::apply).orElse(null)));
                }

                V result = before.apply(t);
                return ofNullable(result).map(after).orElse(null);
            }

            @Override
            public String toString() {
                return before.toString();
            }

            public  <Q> Function<Q, R> compose(Function<? super Q, ? extends T> before) {
                return getSequentialDescribedFunction(before, this);
            }

            public  <U> Function<T, U> andThen(Function<? super R, ? extends U> after) {
                return getSequentialDescribedFunction(this, after);
            }
        };
    }

    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return getSequentialDescribedFunction(before, this);
    }

    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return getSequentialDescribedFunction(this, after);
    }
}
