package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class StoryWriter {

    private interface DescribedConsumer<T> extends Consumer<T> {
        default Consumer<T> andThen(Consumer<? super T> after) {
            requireNonNull(after);
            requireNonNull(DescribedConsumer.class.isAssignableFrom(after.getClass()),
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

    public static <T> Consumer<T> action(String description, Consumer<T> consumer) {
        checkArgument(!isBlank(description), "Description should not be empty");
        return new DescribedConsumer<T>() {
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
        return new Function<>() {
            @Override
            public R apply(T t) {
                return function.apply(t);
            }

            @Override
            public String toString() {
                return description;
            }

            public <F> Function<F, R> compose(Function<? super F, ? extends T> before) {
                requireNonNull(before);
                Function<T, R> after = this;
                return new Function<>() {
                    @Override
                    public R apply(F f) {
                        T result = before.apply(f);
                        return ofNullable(result).map(after).orElse(null);
                    }

                    @Override
                    public String toString() {
                        return before.toString() + " and then return " + after.toString();
                    }
                };
            }

            public <F> Function<T, F> andThen(Function<? super R, ? extends F> after) {
                requireNonNull(after);
                Function<T, R> before = this;

                return new Function<>() {
                    @Override
                    public F apply(T t) {
                        R result = before.apply(t);
                        return ofNullable(result).map(after).orElse(null);
                    }

                    @Override
                    public String toString() {
                        return before.toString() + " and then return " + after.toString();
                    }
                };
            }
        };
    }

    public static <T> Predicate<T> condition(String description, Predicate<T> predicate) {
        return new Predicate<>() {
            @Override
            public boolean test(T t) {
                return predicate.test(t);
            }

            @Override
            public String toString() {
                return description;
            }
        };
    }
}
