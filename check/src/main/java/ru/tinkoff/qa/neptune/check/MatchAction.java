package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.util.Objects;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

/**
 * This class is designed to perform the matching of values.
 *
 * @param <T> is a type of an object to be checked. Also this may be some object that is used to evaluate a
 *            value to be checked
 * @param <R> is a generic type of {@link Matcher}
 */
@SequentialActionSupplier.DefinePerformImperativeParameterName("Assert:")
@MaxDepthOfReporting(1)
public class MatchAction<T, R> extends SequentialActionSupplier<T, R, MatchAction<T, R>> {

    private final String assertDescription;
    private final Matcher<? super R> criteria;

    MatchAction(String assertDescription, Matcher<? super R> criteria) {
        super();
        checkArgument(!Objects.equals(EMPTY, valueOf(assertDescription).trim()),
                "Description shouldn't be an empty string");
        this.assertDescription = assertDescription;
        this.criteria = criteria;
    }

    /**
     * Creates an instance that performs an object matching.
     *
     * @param matcher is a criteria matcher
     * @param <T>     is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    @Description("{matcher}")
    public static <T> MatchAction<T, T> match(@DescriptionFragment("matcher") Matcher<? super T> matcher) {
        return new MatchAction<T, T>(null, matcher)
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
    @Description("{description} {matcher}")
    public static <T, R> MatchAction<T, R> match(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = StepParameter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, R> eval,
            @DescriptionFragment("matcher") Matcher<? super R> matcher) {
        var translated = translate(description);
        return new MatchAction<T, R>(translated, matcher)
                .performOn(new CalculateGetSupplier<>(eval).setDescription(translated));
    }

    @Override
    protected void howToPerform(R value) {
        ofNullable(assertDescription).ifPresentOrElse(
                s -> assertThat(s, value, criteria),
                () -> assertThat(value, criteria));
    }
}
