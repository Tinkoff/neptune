package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;

@ru.tinkoff.qa.neptune.core.api.steps.annotations.Description("{delegateDescription}")
final class MatcherWithTime<T> extends NeptuneFeatureMatcher<T> {

    private final Duration waitForMatch;
    @DescriptionFragment(value = "delegateDescription", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<T> delegateTo;

    MatcherWithTime(Duration waitForMatch, Matcher<T> delegateTo) {
        super(false);
        if (waitForMatch != null) {
            checkArgument(!waitForMatch.isNegative(), "Time value should not be negative");
        }
        this.waitForMatch = waitForMatch;
        this.delegateTo = delegateTo;
    }

    protected boolean checkFeature(Object actual) {

        if (actual == null) {
            return super.checkFeature(null);
        }

        long millis = waitForMatch.toMillis();
        var startTime = currentTimeMillis();
        var time = startTime;
        while (time <= startTime + millis) {
            if (super.checkFeature(actual)) {
                return true;
            }

            time = currentTimeMillis();
        }

        return false;
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var result = delegateTo.matches(toMatch);
        if (!result) {
            appendMismatchDescription(delegateTo, toMatch);
        }
        return result;
    }

    @Override
    protected final void appendMismatchDescription(MismatchDescriber describer) {
        super.appendMismatchDescription(ofNullable(waitForMatch)
                .map(d -> (MismatchDescriber) new MismatchDescriberWithTime(d, describer.toString()))
                .orElse(describer));
    }


    protected final void appendMismatchDescription(Description description) {
        super.appendMismatchDescription(ofNullable(waitForMatch)
                .map(d -> new StringDescription().appendText(new MismatchDescriberWithTime(d, description.toString()).toString()))
                .orElse(description));
    }
}
