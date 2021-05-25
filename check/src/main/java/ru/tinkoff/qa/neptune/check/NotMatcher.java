package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.check.parameter.value.getters.NotParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

@Description("{notExpression}")
final class NotMatcher<T> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment(value = "notExpression", makeReadableBy = NotParameterValueGetter.class)
    private final Matcher<T> toInvert;

    NotMatcher(Matcher<T> toInvert) {
        checkNotNull(toInvert);
        this.toInvert = toInvert;
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var result = !toInvert.matches(toMatch);

        if (!result) {
            appendMismatchDescription(new StringDescription().appendText(concatMatcherDescriptions(toInvert)));
        }

        return result;
    }
}
