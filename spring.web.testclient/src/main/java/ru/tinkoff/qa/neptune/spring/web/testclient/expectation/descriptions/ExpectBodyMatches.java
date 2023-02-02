package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Body matches: {matcher}")
public final class ExpectBodyMatches extends SelfDescribed {

    @DescriptionFragment("matcher")
    final Matcher<?> matcher;

    public ExpectBodyMatches(Matcher<?> matcher) {
        this.matcher = matcher;
    }
}
