package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation.*;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

interface Condition<T> extends Predicate<T> {

    default Predicate<T> xor(Predicate<? super T> other) {
        requireNonNull(other);
        return (t) -> test(t) ^ other.test(t);
    }

    class DescribedCondition<T> implements Condition<T>, TurnsRetortingOff<DescribedCondition<T>> {

        private final String description;
        private final Predicate<T> predicate;
        private boolean toNotReport = false;

        DescribedCondition(String description, Predicate<T> predicate) {
            checkArgument(nonNull(predicate), "Predicate should be defined");
            checkArgument(!isBlank(description), "Description should not be empty");

            this.description = description;
            this.predicate = predicate;
        }

        private static String getDescription(Predicate<?> toBeDescribed) {
            String description;
            if (isLoggable(toBeDescribed)) {
                if (DescribedCondition.class.isAssignableFrom(toBeDescribed.getClass()) &&
                        ((DescribedCondition<?>) toBeDescribed).toNotReport) {
                    return EMPTY;
                }
                description = toBeDescribed.toString();
            }
            else {
                description = "<not described condition>";
            }
            return description;
        }

        private static String getDescription(Predicate<?> p1, Predicate<?> p2,
                                             ConditionConcatenation conditionConcatenation) {
            var description1 = getDescription(p1);
            var description2 = getDescription(p2);
            if (isBlank(description1) && isBlank(description2)) {
                return EMPTY;
            }

            if (isBlank(description1) ^ isBlank(description2)) {
                return format("%s%s", description1, description2).trim();
            }

            if (AND.equals(conditionConcatenation)) {
                return format("%s%s %s", description1, AND, description2).trim();
            }

            return format("(%s) %s (%s)", description1, conditionConcatenation, description2).trim();
        }

        @Override
        public boolean test(T t) {
            return predicate.test(t);
        }

        public String toString() {
            return description;
        }

        @Override
        public Predicate<T> and(Predicate<? super T> other) {
            checkNotNull(other);
            var description = getDescription(this, other, AND);
            if (isBlank(description)) {
                return t -> test(t) && other.test(t);
            }
            return new DescribedCondition<>(description, t -> test(t) && other.test(t));
        }

        @Override
        public Predicate<T> negate() {
            return new DescribedCondition<>(format("not [%s]", toString()), t -> !test(t));
        }

        public Predicate<T> or(Predicate<? super T> other) {
            checkNotNull(other);
            var description = getDescription(this, other, OR);
            if (isBlank(description)) {
                return t -> test(t) || other.test(t);
            }
            return new DescribedCondition<>(description, t -> test(t) || other.test(t));
        }

        @Override
        public Predicate<T> xor(Predicate<? super T> other) {
            checkNotNull(other);
            var description = getDescription(this, other, XOR);
            if (isBlank(description)) {
                return t -> test(t) ^ other.test(t);
            }
            return new DescribedCondition<>(description, t -> test(t) ^ other.test(t));
        }

        @Override
        public DescribedCondition<T> turnReportingOff() {
            toNotReport = true;
            return this;
        }
    }
}
