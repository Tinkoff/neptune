package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;

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

    MatchAction(Matcher<? super R> criteria) {
        super();
        this.criteria = criteria;
    }

    /**
     * Creates an instance that performs an object matching.
     *
     * @param matcher is a criteria matcher
     * @param <T>     is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    @Description("Check object. Assert: {matcher}")
    public static <T> MatchAction<T, T> match(@DescriptionFragment("matcher") Matcher<? super T> matcher) {
        return new MatchAction<T, T>(matcher)
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
    @Description("Check {description}. Assert: {matcher}")
    public static <T, R> MatchAction<T, R> match(@DescriptionFragment("description") String description, Function<T, R> eval, @DescriptionFragment("matcher") Matcher<? super R> matcher) {
        return new MatchAction<T, R>(matcher)
                .performOn(new CalculateGetSupplier<>(eval).setDescription(description));
    }

    @Override
    protected void performActionOn(R value) {
        assertThat(value, new InnerMatcher<>(this.toString(), criteria));
    }
}
