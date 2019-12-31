package ru.tinkoff.qa.neptune.core.api.steps.context;

import static ru.tinkoff.qa.neptune.core.api.steps.context.ConstructorParameters.params;

class ProviderOfEmptyParameters implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params();
    }
}
