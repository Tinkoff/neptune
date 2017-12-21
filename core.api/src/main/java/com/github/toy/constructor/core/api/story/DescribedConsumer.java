package com.github.toy.constructor.core.api.story;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

interface DescribedConsumer<T> extends Consumer<T> {
    default Consumer<T> andThen(Consumer<? super T> after) {
        requireNonNull(after);
        checkArgument(DescribedConsumer.class.isAssignableFrom(after.getClass()),
                "It seems given consumer doesn't describe any action. Use method " +
                        "StoryWriter.action to describe the after-action.");
        Consumer<T> before = this;

        return new DescribedConsumer<T>() {
            @Override
            public void accept(T t) {
                before.accept(t); after.accept(t);
            }

            public String toString() {
                return before.toString() + " \n and then -> \n" + after.toString();
            }
        };
    }
}
