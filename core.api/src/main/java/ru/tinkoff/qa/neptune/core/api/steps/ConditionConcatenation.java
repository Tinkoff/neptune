package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * For concatenation of predicates and the building of AND, OR, XOR and NOT expressions
 */
@SuppressWarnings("unchecked")
public enum ConditionConcatenation {
    AND {
        @Override
        <T> Predicate<T> concat(Predicate<T> p1, Predicate<T> p2) {
            return p1.and(p2);
        }
    },
    OR {
        @Override
        <T> Predicate<T> concat(Predicate<T> p1, Predicate<T> p2) {
            return p1.or(p2);
        }
    },
    XOR {
        @Override
        <T> Predicate<T> concat(Predicate<T> p1, Predicate<T> p2) {
            checkNotNull(p1);
            checkNotNull(p2);
            if (Condition.class.isAssignableFrom(p1.getClass())) {
                return ((Condition) p1).xor(p2);
            }

            if (Condition.class.isAssignableFrom(p2.getClass())) {
                return ((Condition) p2).xor(p1);
            }

            return ((Condition<T>) p1::test).xor(p2);
        }
    };

    public static <T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }

    abstract <T> Predicate<T> concat(Predicate<T> p1, Predicate<T> p2);
}
