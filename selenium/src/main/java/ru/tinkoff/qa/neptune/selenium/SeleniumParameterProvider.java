package ru.tinkoff.qa.neptune.selenium;

import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ParameterProvider;

import static ru.tinkoff.qa.neptune.core.api.ConstructorParameters.params;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;

public class SeleniumParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.get());
    }
}
