package com.github.toy.constructor.core.api;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

interface DescribedConsumer<T> extends Consumer<T> {

    String DELIMITER = "->\n";

    private static String tabs(String description) {
        int delimiters = description.split(DELIMITER).length;
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= delimiters; i++) {
            result.append(" ");
        }
        return result.toString();
    }

    private static <T> Consumer<T> getSequentialDescribedConsumer(Consumer<? super T> before,
                                                                  Consumer<? super T> after) {
        checkNotNull(before);
        checkNotNull(after);
        checkArgument(DescribedConsumer.class.isAssignableFrom(before.getClass()),
                "It seems given consumer doesn't describe any before-action. Use method " +
                        "StoryWriter.action to describe the after-action.");
        checkArgument(DescribedConsumer.class.isAssignableFrom(after.getClass()),
                "It seems given consumer doesn't describe any after-action. Use method " +
                        "StoryWriter.action to describe the after-action.");

        return new DescribedConsumer<>() {
            @Override
            public void accept(T t) {
                before.accept(t); after.accept(t);
            }

            public String toString() {
                return format("%s ->\n%s%s", before, tabs(before.toString()), after);
            }

            public Consumer<T> andThen(Consumer<? super T> afterAction)  {
                return getSequentialDescribedConsumer(this, afterAction);
            }
        };
    }

    default Consumer<T> andThen(Consumer<? super T> afterAction)  {
        return getSequentialDescribedConsumer(this, afterAction);
    }
}
