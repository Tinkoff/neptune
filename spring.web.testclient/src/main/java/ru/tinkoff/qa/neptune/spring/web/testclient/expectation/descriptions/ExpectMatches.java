package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("'{description}' matches: {matcher}")
public final class ExpectMatches {

    @DescriptionFragment("description")
    final String description;

    @DescriptionFragment("matcher")
    final Matcher<?> matcher;

    public ExpectMatches(String description, Matcher<?> matcher) {
        this.description = description;
        this.matcher = matcher;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
