package ru.tinkoff.qa.neptune.core.api.steps.context;

public interface ParameterProvider {
    /**
     * Creates an array of parameters of invocation of a constrictor
     * of {@link Context} subclass.
     *
     * @return parameters of invocation of a constrictor of {@link Context} subclass.
     */
    Object[] provide();
}
