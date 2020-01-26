package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public final class Criteria<T> implements Supplier<Predicate<T>> {

    private final String description;
    private final Predicate<? super T> howToCheck;

    private Criteria(String description, Predicate<? super T> howToCheck) {
        checkArgument(isNotBlank(description), "Description of the criteria should not be blank or null string");
        checkArgument(nonNull(howToCheck), "An algorithm how to check an object by criteria should be defined");
        this.description = description;
        this.howToCheck = howToCheck;
    }

    public String toString() {
        return description;
    }

    public Predicate<T> get() {
        return (Predicate<T>) howToCheck;
    }

    /**
     *
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> Criteria<T> AND(Criteria<T>... criteria) {
        checkArgument(nonNull(criteria), "List of criteria should not be null");
        checkArgument(criteria.length > 1, "At least two criteria should be defined");

        var description = stream(criteria)
                .map(c -> "(" + c.toString() + ")")
                .collect(joining(" and "));

        Predicate<T> newPredicate = null;
        for (var tCriteria: criteria) {
            newPredicate = ofNullable(newPredicate)
                    .map(predicate -> predicate.and(tCriteria.get()))
                    .orElseGet(tCriteria);
        }

        return new Criteria<>(description, newPredicate);
    }

    /**
     *
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> Criteria<T> OR(Criteria<T>... criteria) {
        checkArgument(nonNull(criteria), "List of criteria should not be null");
        checkArgument(criteria.length > 1, "At least two criteria should be defined");

        var description = stream(criteria)
                .map(c -> "(" + c.toString() + ")")
                .collect(joining(" or "));

        Predicate<T> newPredicate = null;
        for (var tCriteria: criteria) {
            newPredicate = ofNullable(newPredicate)
                    .map(predicate -> predicate.or(tCriteria.get()))
                    .orElseGet(tCriteria);
        }

        return new Criteria<>(description, newPredicate);
    }

    /**
     *
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> Criteria<T> XOR(Criteria<T>... criteria) {
        checkArgument(nonNull(criteria), "List of criteria should not be null");
        checkArgument(criteria.length > 1, "At least two criteria should be defined");

        var description = stream(criteria)
                .map(c -> "(" + c.toString() + ")")
                .collect(joining(" or "));

        Predicate<T> newPredicate = null;
        for (var tCriteria: criteria) {
            newPredicate = ofNullable(newPredicate)
                    .map(predicate -> predicate.or(tCriteria.get()))
                    .orElseGet(tCriteria);
        }

        return new Criteria<>(description, newPredicate);
    }

    /**
     * 
     * @param description
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> Criteria<T> condition(String description, Predicate<T> predicate) {
        return new Criteria<>(description, predicate);
    }
}
