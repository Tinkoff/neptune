package com.github.toy.constructor.core.api;

import static com.github.toy.constructor.core.api.ConstructorParameters.params;

public class ProviderOfEmptyParameters implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params();
    }
}
