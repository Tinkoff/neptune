package com.github.toy.constructor.core.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DescriptionUtil {

    public static <T> Consumer<T> c(String description, Consumer<T> consumer) {
        return new Consumer<>() {
            @Override
            public void accept(T t) {
                consumer.accept(t);
            }

            public String toString() {
                return description;
            }
        };
    }

    public static <T, R> Function<T, R> f(String description, Function<T, R> function) {
        return new Function<>() {
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

    public static <T> Predicate<T> p(String description, Predicate<T> predicate) {
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
