package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

public class ABParameterProvider implements ParameterProvider {
    @Override
    public Object[] provide() {
        return new Object[]{1, 2};
    }
}
