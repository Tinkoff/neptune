package com.github.toy.constructor.core.api;

import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.AsIsPredicate.AS_IS;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

interface DescribedPredicate<T> extends Predicate<T> {

    default Predicate<T> and(Predicate<? super T> other) {
        checkNotNull(other);
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
                if (!AS_IS.equals(other)) {
                    return format("%s, %s", thisCondition.toString(), other.toString());
                }
                return thisCondition.toString();
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
                return format("not (%s)", thisCondition.toString());
            }
        };
    }

    default Predicate<T> or(Predicate<? super T> other) {
        checkNotNull(other);
        checkArgument(DescribedPredicate.class.isAssignableFrom(other.getClass()),
                "It seems given predicate doesn't describe any condition. Use method " +
                        "StoryWriter.condition to describe the OR-condition.");

        Predicate<T> thisCondition = this;
        return new DescribedPredicate<>() {

            @Override
            public boolean test(T t) {
                if (!AS_IS.equals(other)) {
                    return thisCondition.test(t) || other.test(t);
                }
                return thisCondition.test(t);
            }

            @Override
            public String toString() {
                if (!AS_IS.equals(other)) {
                    return format("(%s) or (%s)", thisCondition.toString(), other.toString());
                }
                return thisCondition.toString();
            }
        };
    }
}
