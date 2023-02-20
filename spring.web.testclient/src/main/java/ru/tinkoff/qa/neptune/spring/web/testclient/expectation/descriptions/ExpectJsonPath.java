package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Body. Json path '{jsonPath}'. Arguments '{args}'")
public final class ExpectJsonPath extends SelfDescribed {

    @DescriptionFragment("jsonPath")
    final String jsonPath;

    @DescriptionFragment(value = "args", makeReadableBy = ArgParameterValueGetter.class)
    final Object[] args;

    public ExpectJsonPath(String jsonPath, Object... args) {
        this.jsonPath = jsonPath;
        this.args = args;
    }
}
