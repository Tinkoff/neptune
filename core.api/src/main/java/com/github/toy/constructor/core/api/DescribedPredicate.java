package com.github.toy.constructor.core.api;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.sun.tools.javac.util.Assert.checkNonNull;
import static java.lang.String.format;

interface DescribedPredicate<T> extends Predicate<T> {

    default Predicate<T> and(Predicate<? super T> other) {
        checkNonNull(other);
        checkArgument(DescribedPredicate.class.isAssignableFrom(other.getClass()),
                "It seems given predicate doesn't describe any condition. Use method " +
                        "StoryWriter.condition to describe the AND-condition.");

        Predicate<T> thisCondition = this;
        return new DescribedPredicate<>() {

            @Override
            public boolean test(T t) {
                return thisCondition.test(t) && other.test(t);
            }

            @Override
            public String toString() {
                return format("%s AND %s", thisCondition.toString(), other.toString());
            }
        };
    }

    default Predicate<T> negate() {
        Predicate<T> thisCondition = this;

        return new DescribedPredicate<>() {
            @Override
            public boolean test(T t) {
                return !thisCondition.test(t);
            }

            @Override
            public String toString() {
                return format("NOT %s", thisCondition.toString());
            }
        };
    }

    default Predicate<T> or(Predicate<? super T> other) {
        checkNonNull(other);
        checkArgument(DescribedPredicate.class.isAssignableFrom(other.getClass()),
                "It seems given predicate doesn't describe any condition. Use method " +
                        "StoryWriter.condition to describe the OR-condition.");

        Predicate<T> thisCondition = this;
        return new DescribedPredicate<>() {

            @Override
            public boolean test(T t) {
                return thisCondition.test(t) || other.test(t);
            }

            @Override
            public String toString() {
                return format("%s OR %s", thisCondition.toString(), other.toString());
            }
        };
    }
}
