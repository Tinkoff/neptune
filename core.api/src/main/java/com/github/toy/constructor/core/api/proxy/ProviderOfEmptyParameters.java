package com.github.toy.constructor.core.api.proxy;

import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;

public class ProviderOfEmptyParameters implements ParameterProvider {
    @Override
    public ConstructorParameters provide() {
        return params();
    }
}
