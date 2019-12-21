package ru.tinkoff.qa.neptune.core.api.steps.context;

public interface ParameterProvider {
    /**
     * Creates an instance which wraps parameters for constructor invocation of some subclass of {@link Context}
     *
     * @return instance of {@link ConstructorParameters}.
     */
    ConstructorParameters provide();
}
