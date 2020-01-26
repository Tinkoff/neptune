package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.steps.Condition.condition;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * For concatenation of predicates and the building of AND, OR, XOR and NOT expressions
 */
@SuppressWarnings("unchecked")
@Deprecated(since = "0.11.4-ALPHA", forRemoval = true)
public enum ConditionConcatenation {
    AND {
        @Override
        <T> Predicate<T> concat(Predicate<T> p1, Predicate<? super T> p2) {
            checkNotNull(p1);
            checkNotNull(p2);

            if (Condition.DescribedCondition.class.isAssignableFrom(p1.getClass())) {
                return p1.and(p2);
            }

            if (Condition.DescribedCondition.class.isAssignableFrom(p2.getClass())) {
                ((Predicate<T>) p2).and(p1);
            }

            if (isLoggable(p1) && isLoggable(p2)) {
                return condition(p1.toString(), p1)
                        .and(condition(p2.toString(), p2));
            }

            if (isLoggable(p1)) {
                return condition(p1.toString(), p1)
                        .and(p2);
            }

            if (isLoggable(p2)) {
                return condition(p2.toString(), (Predicate<T>) p2)
                        .and(p1);
            }

            return condition("<not described condition>", p1)
                    .and(condition("<not described condition>", p2));
        }
    },
    OR {
        @Override
        <T> Predicate<T> concat(Predicate<T> p1, Predicate<? super T> p2) {
            checkNotNull(p1);
            checkNotNull(p2);
            if (Condition.DescribedCondition.class.isAssignableFrom(p1.getClass())) {
                return p1.or(p2);
            }

            if (Condition.DescribedCondition.class.isAssignableFrom(p2.getClass())) {
                ((Predicate<T>) p2).or(p1);
            }

            if (isLoggable(p1) && isLoggable(p2)) {
                return condition(p1.toString(), p1)
                        .or(condition(p2.toString(), p2));
            }

            if (isLoggable(p1)) {
                return condition(p1.toString(), p1)
                        .or(p2);
            }

            if (isLoggable(p2)) {
                return condition(p2.toString(), (Predicate<T>) p2)
                        .or(p1);
            }

            return condition("<not described condition>", p1)
                    .or(condition("<not described condition>",  p2));
        }
    },
    XOR {
        @Override
        <T> Predicate<T> concat(Predicate<T> p1, Predicate<? super T> p2) {
            checkNotNull(p1);
            checkNotNull(p2);
            if (Condition.DescribedCondition.class.isAssignableFrom(p1.getClass())) {
                return ((Condition) p1).xor(p2);
            }

            if (Condition.DescribedCondition.class.isAssignableFrom(p2.getClass())) {
                ((Condition<T>) p2).xor(p1);
            }

            if (isLoggable(p1) && isLoggable(p2)) {
                return ((Condition<T>) condition(p1.toString(), p1))
                        .xor(condition(p2.toString(), p2));
            }

            if (isLoggable(p1)) {
                return ((Condition<T>) condition(p1.toString(), p1))
                        .xor(p2);
            }

            if (isLoggable(p2)) {
                return ((Condition<T>) condition(p2.toString(), (Predicate<T>) p2))
                        .xor(p1);
            }

            return ((Condition<T>) condition("<not described condition>", p1))
                    .xor(condition("<not described condition>", p2));
        }
    };

    abstract <T> Predicate<T> concat(Predicate<T> p1, Predicate<? super T> p2);
}
