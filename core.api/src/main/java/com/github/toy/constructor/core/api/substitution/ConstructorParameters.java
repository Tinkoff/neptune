package com.github.toy.constructor.core.api.substitution;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * POJO which wraps some constructor parameters
 */
public class ConstructorParameters {

    private final Object[] parameterValues;

    public ConstructorParameters(Object[] parameterValues) {
        checkNotNull(parameterValues);
        this.parameterValues = parameterValues;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }
}
