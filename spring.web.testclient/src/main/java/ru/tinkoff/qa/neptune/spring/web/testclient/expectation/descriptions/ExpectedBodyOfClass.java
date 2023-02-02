package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Non null body. Class '{class}'")
public final class ExpectedBodyOfClass extends SelfDescribed {

    @DescriptionFragment("class")
    final Class<?> aClass;

    public ExpectedBodyOfClass(Class<?> aClass) {
        this.aClass = aClass;
    }
}
