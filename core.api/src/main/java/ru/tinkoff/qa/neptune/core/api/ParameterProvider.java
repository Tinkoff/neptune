package ru.tinkoff.qa.neptune.core.api;

public interface ParameterProvider {
    /**
     * Creates an instance which wraps parameters for constructor invocation of some subclass of {@link PerformActionStep}
     * or {@link GetStep}.
     *
     * @return instance of {@link ConstructorParameters}.
     */
    ConstructorParameters provide();
}
