package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.StepAction.action;

/**
 * This class is designed to perform the matching of values.
 *
 * @param <T> is a type of an object to be checked. Also this may be some object that is used to evaluate a
 *           value to be checked
 * @param <R> is a generic type of {@link Matcher}
 */
public abstract class MatchAction<T, R> implements Consumer<T>, Supplier<Consumer<T>> {

    private final Matcher<? super R> criteria;
    private final String description;

    MatchAction(String description, Matcher<? super R> criteria) {
        checkArgument(isNotBlank(description), "Description of a value to be evaluated and checked should not be blank");
        checkArgument(nonNull(criteria), "Criteria matcher should not be null");
        this.description = description;
        this.criteria = criteria;
    }

    /**
     * Creates an instance that performs an object matching.
     *
     * @param matcher is a criteria matcher
     * @param <T> is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    public static <T> MatchAction<T, T> match(Matcher<? super T> matcher) {
        return new DirectMatchAction<>(matcher);
    }

    /**
     * Creates an instance that performs a value matching evaluated out of inspected object
     *
     * @param description of evaluated value
     * @param eval a function that performs evaluation
     * @param matcher is a criteria matcher
     * @param <T> is a type of a value to be checked
     * @param <R> is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    public static <T, R> MatchAction<T, R> match(String description, Function<T, R> eval, Matcher<? super R> matcher) {
        return new EvaluateAndMatchAction<>(description, matcher, eval);
    }

    @Override
    public Consumer<T> get() {
        var action =  action(format("Check %s. Assert: %s", description, criteria), this);
        action.onFinishMakeCaptureOfType(Object.class);
        return action;
    }

    String getDescription() {
        return description;
    }

    Matcher<? super R> getCriteria() {
        return criteria;
    }
}
