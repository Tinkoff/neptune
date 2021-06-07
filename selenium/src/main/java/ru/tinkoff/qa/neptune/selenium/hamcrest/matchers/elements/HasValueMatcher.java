package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;

@Description("element value: {criteria}")
public final class HasValueMatcher<Q, T extends SearchContext & HasValue<Q>> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment(value = "criteria", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super Q> criteria;

    private HasValueMatcher(Matcher<? super Q> criteria) {
        super(true);
        checkArgument(nonNull(criteria), "Matcher to check value should be defined.");
        this.criteria = criteria;
    }

    /**
     * Creates an instance of {@link HasValueMatcher} that checks value of the element.
     *
     * @param value is expected to be equal to value of the element.
     * @param <Q>   type of a value
     * @param <T>   type of an element
     * @return instance of {@link HasValueMatcher}
     */
    public static <Q, T extends SearchContext & HasValue<Q>> Matcher<T> hasValue(Q value) {
        return new HasValueMatcher<>(equalTo(value));
    }

    /**
     * Creates an instance of {@link HasValueMatcher} that checks value of the element.
     *
     * @param value matcher that is supposed to be used for the value verification
     * @param <Q>   type of a value
     * @param <T>   type of an element
     * @return instance of {@link HasValueMatcher}
     */
    public static <Q, T extends SearchContext & HasValue<Q>> Matcher<T> hasValue(Matcher<? super Q> value) {
        return new HasValueMatcher<>(value);
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        Q value = toMatch.getValue();
        var result = criteria.matches(value);
        if (!result) {
            appendMismatchDescription(criteria, value);
        }
        return result;
    }
}
