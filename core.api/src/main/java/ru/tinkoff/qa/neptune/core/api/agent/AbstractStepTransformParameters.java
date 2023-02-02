package ru.tinkoff.qa.neptune.core.api.agent;

public abstract class AbstractStepTransformParameters implements NeptuneTransformParameters {

    @Override
    public Class<?> interceptor() {
        return DescribedStepInterceptor.class;
    }
}
