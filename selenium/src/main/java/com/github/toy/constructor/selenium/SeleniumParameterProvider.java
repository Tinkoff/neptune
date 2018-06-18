package com.github.toy.constructor.selenium;

import com.github.toy.constructor.core.api.ConstructorParameters;
import com.github.toy.constructor.core.api.ParameterProvider;

import static com.github.toy.constructor.core.api.ConstructorParameters.params;
import static com.github.toy.constructor.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;

public class SeleniumParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params(new WrappedWebDriver(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.get()));
    }
}
