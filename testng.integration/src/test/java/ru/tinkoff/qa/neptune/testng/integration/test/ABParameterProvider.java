package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.steps.context.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import static ru.tinkoff.qa.neptune.core.api.steps.context.ConstructorParameters.params;

public class ABParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params(1,2);
    }
}
