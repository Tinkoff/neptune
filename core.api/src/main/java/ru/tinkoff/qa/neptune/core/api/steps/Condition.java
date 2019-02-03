package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

interface Condition<T> extends Predicate<T> {

    default Predicate<T> xor(Predicate<? super T> other) {
        requireNonNull(other);
        return (t) -> test(t) ^ other.test(t);
    }

    class DescribedCondition<T> implements Condition<T> {

        private final String description;
        private final Predicate<T> predicate;

        DescribedCondition(String description, Predicate<T> predicate) {
            checkArgument(nonNull(predicate), "Predicate should be defined");
            checkArgument(!isBlank(description), "Description should not be empty");

            this.description = description;
            this.predicate = predicate;
        }

        private static String getOtherDescription(Predicate<?> other) {
            String otherDescription;
            if (isLoggable(other)) {
                otherDescription = other.toString();
            }
            else {
                otherDescription = "<not described condition>";
            }
            return otherDescription;
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
            return new DescribedCondition<>(format("%s, %s", toString(), getOtherDescription(other)),
                    t -> test(t) && other.test(t));
        }

        @Override
        public Predicate<T> negate() {
            return new DescribedCondition<>(format("not [%s]", toString()), t -> !test(t));
        }

        public Predicate<T> or(Predicate<? super T> other) {
            checkNotNull(other);
            return new DescribedCondition<>(format("(%s) or (%s)", toString(), getOtherDescription(other)),
                    t -> test(t) || other.test(t));
        }

        @Override
        public Predicate<T> xor(Predicate<? super T> other) {
            checkNotNull(other);
            return new DescribedCondition<>(format("(%s) xor (%s)", toString(), getOtherDescription(other)),
                    t -> test(t) ^ other.test(t));
        }
    }
}
