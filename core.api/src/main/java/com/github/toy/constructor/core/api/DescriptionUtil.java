package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public final class DescriptionUtil {

    public static <T> Consumer<T> action(String description, Consumer<T> consumer) {
        return new Consumer<>() {
            @Override
            public void accept(T t) {
                consumer.accept(t);
            }

            public String toString() {
                return description;
            }

            @Override
            public Consumer<T> andThen(Consumer<? super T> after) {
                Consumer<T> before = this;
                requireNonNull(after);

                return new Consumer<>() {
                    @Override
                    public void accept(T t) {
                        before.accept(t); after.accept(t);
                    }

                    public String toString() {
                        return before.toString() + " and then \n\n" + after.toString();
                    }
                };
            }
        };
    }

    public static <T, R> Function<T, R> toReturn(String description, Function<T, R> function) {
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
