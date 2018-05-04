package com.github.toy.constructor.core.api;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unchecked")
class DescribedFunction<T, R> implements Function<T, R> {

    private final LinkedList<Function<Object, Object>> sequence = new LinkedList<>();
    private final String description;
    private final Function<T, R> function;

    DescribedFunction(String description, Function<T, R> function) {
        checkArgument(function != null, "Function should be defined");
        checkArgument(!isBlank(description), "Description should not be empty");
        this.description = description;
        this.function = function;
        if (DescribedFunction.class.isAssignableFrom(function.getClass())) {
            addToSequence(DescribedFunction.class.cast(function));
        }
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
        })
                .addToSequence(DescribedFunction.class.cast(before))
                .addToSequence(DescribedFunction.class.cast(after));
    }

    @Override
    public R apply(T t) {
        return function.apply(t);
    }

    LinkedList<Function<Object, Object>> getSequence() {
        return sequence;
    }

    private DescribedFunction<T, R> addToSequence(DescribedFunction<?, ?> sequenceChain) {
        List<Function<Object, Object>> functions = sequenceChain.getSequence();
        if (sequenceChain.getSequence().size() == 0) {
           sequence.addLast((Function<Object, Object>) sequenceChain);
        }
        else {
            sequence.addAll(functions);
        }
        return this;
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
}
