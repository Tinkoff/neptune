package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public interface Condition<T> extends Predicate<T> {

    String NOT_DESCRIBED = "<not described condition>";

    /**
     * This method creates a predicate with some string description. This predicate is
     * supposed to be used as some conditions or filter.
     *
     * @param description string narration of the conditions
     * @param predicate which checks some input value
     * @param <T> type of the input value
     * @return a new predicate with the given string description. Description is returned
     * by the {@link #toString()} method.
     */
    static <T> Predicate<T> condition(String description, Predicate<T> predicate) {
        return new DescribedCondition<>(description, predicate);
    }

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

        @Override
        public boolean test(T t) {
            return predicate.test(t);
        }

        private static <T> DescribedCondition<T> getDescribedCondition(Predicate<T> p) {
            if (DescribedCondition.class.isAssignableFrom(p.getClass())) {
                return (DescribedCondition<T>) p;
            }

            if (isLoggable(p)) {
                return new DescribedCondition<>(p.toString(), p);
            }

            return new DescribedCondition<>(NOT_DESCRIBED, p);
        }

        private static <T> DescribedCondition<T> getConditionWithChainedDescription(String description,
                                                                                    DescribedCondition<?> p,
                                                                                    DescribedCondition p2,
                                                                                    Predicate<T> toTest) {
            return new DescribedCondition<>(description, toTest) {
                String getReportableDescription() {
                    if (!p.toNotReport && !p2.toNotReport) {
                        return super.getReportableDescription();
                    }

                    if (!p.toNotReport) {
                        return p.description;
                    }

                    if (!p2.toNotReport) {
                        return p2.description;
                    }

                    return EMPTY;
                }
            };
        }

        String getReportableDescription() {
            return description;
        }

        public String toString() {
            if (!toNotReport) {
                return getReportableDescription();
            }
            return EMPTY;
        }

        @Override
        public Predicate<T> and(Predicate<? super T> other) {
            checkNotNull(other);
            var described = getDescribedCondition(other);
            final var thisDescribed = this;

            return getConditionWithChainedDescription(format("%s, %s", this, described), this, described,
                    t -> thisDescribed.test(t) && other.test(t));
        }

        @Override
        public Predicate<T> negate() {
            var result = new DescribedCondition<T>(format("not [%s]", description), t -> !test(t));
            result.toNotReport = toNotReport;
            return result;
        }

        public Predicate<T> or(Predicate<? super T> other) {
            checkNotNull(other);
            var described = getDescribedCondition(other);
            final var thisDescribed = this;

            return getConditionWithChainedDescription(format("(%s) or (%s)", this, described), this, described,
                    t -> thisDescribed.test(t) || other.test(t));
        }

        @Override
        public Predicate<T> xor(Predicate<? super T> other) {
            checkNotNull(other);
            var described = getDescribedCondition(other);
            final var thisDescribed = this;

            return getConditionWithChainedDescription(format("(%s) xor (%s)", this, described), this, described,
                    t -> thisDescribed.test(t) ^ other.test(t));
        }

        @Override
        public DescribedCondition<T> turnReportingOff() {
            toNotReport = true;
            return this;
        }
    }
}
