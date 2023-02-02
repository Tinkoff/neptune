package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.Map;

import static java.util.Objects.isNull;

@Description("Body. Xpath '{xPath}'. Namespaces '{nameSpaces}'. Arguments '{args}'")
public final class ExpectXpath extends SelfDescribed {

    @DescriptionFragment("xPath")
    final String xPath;

    @DescriptionFragment("nameSpaces")
    final Map<String, String> nameSpaces;

    @DescriptionFragment(value = "args", makeReadableBy = ArgParameterValueGetter.class)
    final Object[] args;

    public ExpectXpath(String xPath, Map<String, String> nameSpaces, Object[] args) {
        this.xPath = xPath;
        this.nameSpaces = isNull(nameSpaces) ? Map.of() : nameSpaces;
        this.args = args;
    }
}
