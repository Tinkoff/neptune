package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Body as xml-string is equal to or contains same elements as '{xml}'")
public final class ExpectedBodyXml extends SelfDescribed {

    @DescriptionFragment("xml")
    final String xmlString;

    public ExpectedBodyXml(String xmlString) {
        this.xmlString = xmlString;
    }
}
