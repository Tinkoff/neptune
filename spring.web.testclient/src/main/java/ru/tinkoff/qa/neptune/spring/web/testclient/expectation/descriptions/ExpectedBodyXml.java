package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Body as xml-string is equal to or contains same elements as '{xml}'")
public final class ExpectedBodyXml {

    @DescriptionFragment("xml")
    final String xmlString;

    public ExpectedBodyXml(String xmlString) {
        this.xmlString = xmlString;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
