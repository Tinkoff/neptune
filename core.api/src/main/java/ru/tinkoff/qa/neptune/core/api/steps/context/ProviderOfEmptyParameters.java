package ru.tinkoff.qa.neptune.core.api.steps.context;

import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;

import static ru.tinkoff.qa.neptune.core.api.ConstructorParameters.params;

public class ProviderOfEmptyParameters implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params();
    }
}
