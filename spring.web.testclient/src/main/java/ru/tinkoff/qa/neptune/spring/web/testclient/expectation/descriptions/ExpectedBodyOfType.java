package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import org.springframework.core.ParameterizedTypeReference;
import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Non null body. Type '{type}'")
public final class ExpectedBodyOfType extends SelfDescribed {

    @DescriptionFragment("type")
    final ParameterizedTypeReference<?> type;

    public ExpectedBodyOfType(ParameterizedTypeReference<?> type) {
        this.type = type;
    }
}
