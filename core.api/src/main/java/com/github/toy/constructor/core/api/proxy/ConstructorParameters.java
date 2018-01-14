package com.github.toy.constructor.core.api.proxy;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * POJO which wraps some constructor parameters
 */
public class ConstructorParameters {

    private final Object[] parameterValues;

    public static ConstructorParameters params(Object... parameterValues) {
        return new ConstructorParameters(parameterValues);
    }

    private ConstructorParameters(Object[] parameterValues) {
        checkNotNull(parameterValues);
        this.parameterValues = parameterValues;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }
}
