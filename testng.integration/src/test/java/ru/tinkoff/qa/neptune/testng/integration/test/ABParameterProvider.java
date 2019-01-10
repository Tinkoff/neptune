package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ParameterProvider;

import static ru.tinkoff.qa.neptune.core.api.ConstructorParameters.params;

public class ABParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params(1,2);
    }
}
