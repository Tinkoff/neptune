package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Body as json-string is equal to or contains same key-value pairs as '{json}'")
public final class ExpectedBodyJson {

    @DescriptionFragment("json")
    final String jsonString;

    public ExpectedBodyJson(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
