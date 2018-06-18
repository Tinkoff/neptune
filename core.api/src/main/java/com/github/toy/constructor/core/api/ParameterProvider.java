package com.github.toy.constructor.core.api;

public interface ParameterProvider {
    /**
     * Creates an instance which wraps parameters for constructor invocation of some subclass of {@link PerformStep}
     * or {@link GetStep}.
     *
     * @return instance of {@link ConstructorParameters}.
     */
    ConstructorParameters provide();
}
