package ru.tinkoff.qa.neptune.core.api.steps.context;

class ProviderOfEmptyParameters implements ParameterProvider {
    @Override
    public Object[] provide() {
        return new Object[]{};
    }
}
