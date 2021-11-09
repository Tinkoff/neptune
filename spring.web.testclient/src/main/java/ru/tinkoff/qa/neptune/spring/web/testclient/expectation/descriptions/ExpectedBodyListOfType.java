package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import org.springframework.core.ParameterizedTypeReference;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Non null list body. Type of list item '{type}'")
public final class ExpectedBodyListOfType {

    @DescriptionFragment("type")
    final ParameterizedTypeReference<?> type;

    public ExpectedBodyListOfType(ParameterizedTypeReference<?> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
