package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Non null list body. Class of list item '{class}'")
public final class ExpectedBodyListOfClass {

    @DescriptionFragment("class")
    final Class<?> aClass;

    public ExpectedBodyListOfClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
