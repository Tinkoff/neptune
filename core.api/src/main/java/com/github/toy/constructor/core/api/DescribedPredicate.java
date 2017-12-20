package com.github.toy.constructor.core.api;

import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

interface DescribedPredicate<T> extends Predicate<T> {

    default Predicate<T> and(Predicate<? super T> other) {
        requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }
}
