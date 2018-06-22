package com.github.toy.constructor.testng.integration.test;

import com.github.toy.constructor.core.api.ConstructorParameters;
import com.github.toy.constructor.core.api.ParameterProvider;

import static com.github.toy.constructor.core.api.ConstructorParameters.params;

public class ABParameterProvider implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params(1,2);
    }
}
