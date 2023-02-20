package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("'{description}' matches: {matcher}")
public final class ExpectMatches extends SelfDescribed {

    @DescriptionFragment("description")
    final String description;

    @DescriptionFragment("matcher")
    final Matcher<?> matcher;

    public ExpectMatches(String description, Matcher<?> matcher) {
        this.description = description;
        this.matcher = matcher;
    }
}
