package com.github.toy.constructor.selenium;

import com.github.toy.constructor.core.api.proxy.ConstructorParameters;
import com.github.toy.constructor.core.api.proxy.ParameterProvider;

import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.selenium.properties.SupportedWebDriverPropertyProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;

public class SeleniumParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params(new WrappedWebDriver(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.get()));
    }
}
