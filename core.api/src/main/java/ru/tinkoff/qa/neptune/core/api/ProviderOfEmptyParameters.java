package ru.tinkoff.qa.neptune.core.api;

import static ru.tinkoff.qa.neptune.core.api.ConstructorParameters.params;

public class ProviderOfEmptyParameters implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params();
    }
}
