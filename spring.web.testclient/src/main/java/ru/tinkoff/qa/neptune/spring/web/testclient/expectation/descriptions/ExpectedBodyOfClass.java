package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Non null body of class {class}")
public final class ExpectedBodyOfClass {

    @DescriptionFragment("class")
    final Class<?> aClass;

    public ExpectedBodyOfClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
