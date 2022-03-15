package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Body matches: {matcher}")
public final class ExpectBodyMatches {

    @DescriptionFragment("matcher")
    final Matcher<?> matcher;

    public ExpectBodyMatches(Matcher<?> matcher) {
        this.matcher = matcher;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
