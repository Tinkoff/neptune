package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.steps.StepFunction.toGet;

/**
 * This class is designed to perform the matching of values.
 *
 * @param <T> is a type of an object to be checked. Also this may be some object that is used to evaluate a
 *            value to be checked
 * @param <R> is a generic type of {@link Matcher}
 */
@MakeCaptureOnFinishing(typeOfCapture = Object.class)
@SequentialActionSupplier.DefaultParameterNames(performOn = "Check evaluation result")
public class MatchAction<T, R> extends SequentialActionSupplier<T, R, MatchAction<T, R>> {

    @StepParameter("Match criteria")
    private final Matcher<? super R> criteria;
    private final String description;

    MatchAction(String description, Matcher<? super R> criteria) {
        super("Check " + description + ". Assert: " + criteria);
        checkArgument(nonNull(criteria), "Criteria matcher should not be null");
        this.criteria = criteria;
        this.description = description;
    }

    /**
     * Creates an instance that performs an object matching.
     *
     * @param matcher is a criteria matcher
     * @param <T>     is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    public static <T> MatchAction<T, T> match(Matcher<? super T> matcher) {
        return new MatchAction<T, T>("object", matcher)
                .performOn(t -> t);
    }

    /**
     * Creates an instance that performs a value matching evaluated out of inspected object
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param matcher     is a criteria matcher
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    public static <T, R> MatchAction<T, R> match(String description, Function<T, R> eval, Matcher<? super R> matcher) {
        return new MatchAction<T, R>(description, matcher)
                .performOn(toGet(description, eval).onFinishMakeCaptureOfType(Object.class));
    }

    @Override
    protected void performActionOn(R value) {
        assertThat(value, new InnerMatcher<>(description, criteria));
    }
}
