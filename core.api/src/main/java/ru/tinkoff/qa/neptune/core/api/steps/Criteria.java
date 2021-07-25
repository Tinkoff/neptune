package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.logical.lexemes.Not.NOT_LEXEME;
import static ru.tinkoff.qa.neptune.core.api.logical.lexemes.OnlyOne.ONLY_ONE_LEXEME;
import static ru.tinkoff.qa.neptune.core.api.logical.lexemes.Or.OR_LEXEME;

/**
 * This class is designed to create a {@link Predicate} used by {@link SequentialGetStepSupplier}
 *
 * @param <T> is a type of a value to be checked/filtered
 */
@SuppressWarnings("unchecked")
public final class Criteria<T> implements Supplier<Predicate<T>> {

    private String description;
    private final Predicate<? super T> howToCheck;

    private Criteria(Predicate<? super T> howToCheck) {
        checkArgument(nonNull(howToCheck), "An algorithm how to check an object by criteria should be defined");
        this.howToCheck = howToCheck;
    }

    /**
     * The joining of defined criteria with AND-condition.
     *
     * @param criteria to be joined
     * @param <T> <T> is a type of a value to be checked/filtered by each criteria
     *           and resulted criteria as well
     * @return a new joined criteria
     */
    @SafeVarargs
    public static <T> Criteria<T> AND(Criteria<T>... criteria) {
        checkArgument(nonNull(criteria), "List of criteria should not be null");
        checkArgument(criteria.length > 1, "At least two criteria should be defined");

        var description = stream(criteria)
                .map(Criteria::toString)
                .collect(joining(", "));

        Predicate<T> newPredicate = null;
        for (var tCriteria: criteria) {
            newPredicate = ofNullable(newPredicate)
                    .map(predicate -> predicate.and(tCriteria.get()))
                    .orElseGet(tCriteria);
        }

        return new Criteria<>(newPredicate).setDescription(description);
    }

    /**
     * The joining of defined criteria with inclusive OR-condition.
     *
     * @param criteria to be joined
     * @param <T>      is a type of a value to be checked/filtered by each criteria
     *                 and resulted criteria as well
     * @return a new joined criteria
     */
    @SafeVarargs
    public static <T> Criteria<T> OR(Criteria<T>... criteria) {
        checkArgument(nonNull(criteria), "List of criteria should not be null");
        checkArgument(criteria.length > 1, "At least two criteria should be defined");

        var description = stream(criteria)
                .map(c -> "(" + c.toString() + ")")
                .collect(joining(" " + OR_LEXEME + " "));


        Predicate<T> newPredicate = null;
        for (var tCriteria : criteria) {
            newPredicate = ofNullable(newPredicate)
                    .map(predicate -> predicate.or(tCriteria.get()))
                    .orElseGet(tCriteria);
        }

        return new Criteria<>(newPredicate).setDescription(description);
    }

    static <T> Criteria<T> AND(List<Criteria<T>> criteria) {
        if (criteria == null) {
            return null;
        }

        if (criteria.size() == 1) {
            return criteria.get(0);
        }

        return AND(Iterables.toArray(criteria, Criteria.class));
    }

    /**
     * The joining of defined criteria with exclusive OR-condition.
     *
     * @param criteria to be joined
     * @param <T>      is a type of a value to be checked/filtered by each criteria
     *                 and resulted criteria as well
     * @return a new joined criteria
     */
    @SafeVarargs
    public static <T> Criteria<T> ONLY_ONE(Criteria<T>... criteria) {
        checkArgument(nonNull(criteria), "List of criteria should not be null");
        checkArgument(criteria.length > 1, "At least two criteria should be defined");

        var description = stream(criteria)
                .map(c -> "(" + c.toString() + ")")
                .collect(joining(" " + ONLY_ONE_LEXEME + " "));

        Predicate<T> newPredicate = t -> stream(criteria)
                .map(tCriteria -> tCriteria.get().test(t))
                .filter(b -> b)
                .count() == 1;

        return new Criteria<>(newPredicate).setDescription(description);
    }

    /**
     * The joining of defined criteria with AND NOT-condition.
     *
     * @param criteria to be joined
     * @param <T> is a type of a value to be checked/filtered
     * @return a new joined criteria
     */
    @SafeVarargs
    public static <T> Criteria<T> NOT(Criteria<T>... criteria) {
        checkArgument(nonNull(criteria), "List of criteria should not be null");
        checkArgument(criteria.length > 0, "At least one criteria should be defined");

        var description = stream(criteria)
                .map(c -> NOT_LEXEME + " (" + c.toString() + ")")
                .collect(joining(", "));

        Predicate<T> newPredicate = null;
        for (var tCriteria: criteria) {
            newPredicate = ofNullable(newPredicate)
                    .map(predicate -> predicate.and(not(tCriteria.get())))
                    .orElseGet(() -> not(tCriteria.get()));
        }

        return new Criteria<>(newPredicate).setDescription(description);
    }

    /**
     * Creates and instance of {@link Criteria}
     *
     * @param description is a string description of the condition
     * @param predicate   is the condition described by {@link Predicate}
     * @param <T>         is a type of a value to be checked/filtered
     * @return a new instance of {@link Criteria}
     */
    public static <T> Criteria<T> condition(String description, Predicate<T> predicate) {
        return condition(predicate).setDescription(translate(description));
    }

    public static <T> Criteria<T> condition(Predicate<T> predicate) {
        return new Criteria<>(predicate);
    }

    /**
     * An auxiliary method that helps to describe some {@link Criteria} when it is necessary to check some string
     * on containing of a substring / regexp matching
     *
     * @param subStringOrRegExp substring or reg exp
     * @return auxiliary predicate
     */
    public static Predicate<String> checkByStringContainingOrRegExp(String subStringOrRegExp) {
        return s -> {
            if (s.contains(subStringOrRegExp)) {
                return true;
            }

            try {
                var pattern = compile(subStringOrRegExp);
                var m = pattern.matcher(s);
                return m.matches();
            } catch (Throwable t) {
                t.printStackTrace();
                return false;
            }
        };
    }

    final Criteria<T> setDescription(String description) {
        checkArgument(isNotBlank(description), "Description of the criteria should not be blank or null string");
        this.description = description;
        return this;
    }

    public String toString() {
        return ofNullable(description).orElse("not described criteria");
    }

    public Predicate<T> get() {
        return (Predicate<T>) howToCheck;
    }
}
