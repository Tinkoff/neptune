package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

@Description("{matcherString}")
final class SimpleMatcher<T> extends MatcherWithTime<T> {

    @DescriptionFragment("matcherString")
    private final Matcher<? super T> delegateTo;

    SimpleMatcher(Matcher<? super T> delegateTo) {
        this.delegateTo = delegateTo;
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var result = delegateTo.matches(toMatch);

        if (!result) {
            var description = new StringDescription();
            delegateTo.describeMismatch(toMatch, description);
            appendMismatchDescription(new StringDescription().appendText(translate(description.toString())));
        }

        return result;
    }
}
