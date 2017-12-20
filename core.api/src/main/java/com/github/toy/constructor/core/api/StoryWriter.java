package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class StoryWriter {

    public static <T> Consumer<T> action(String description, Consumer<T> consumer) {
        checkArgument(!isBlank(description), "Description should not be empty");
        return new DescribedConsumer<>() {
            @Override
            public void accept(T t) {
                consumer.accept(t);
            }

            @Override
            public String toString() {
                return description;
            }
        };
    }

    public static <T, R> Function<T, R> toGet(String description, Function<T, R> function) {
        return new DescribedFunction<>() {
            @Override
            public R apply(T t) {
                return function.apply(t);
            }

            @Override
            public String toString() {
                return description;
            }
        };
    }

    public static <T> Predicate<T> condition(String description, Predicate<T> predicate) {
        return new DescribedPredicate<>() {
            @Override
            public boolean test(T t) {
                return predicate.test(t);
            }

            @Override
            public String toString() {
                return format("With condition %s", description);
            }
        };
    }
}
