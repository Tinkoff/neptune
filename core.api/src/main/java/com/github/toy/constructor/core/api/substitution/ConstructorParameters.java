package com.github.toy.constructor.core.api.substitution;

import java.util.Map;

import static java.util.Map.entry;

/**
 * POJO which wraps some constructor parameters
 */
public class ConstructorParameters {

    private static final Map<Class<?>, Class<?>> FOR_USED_SIMPLE_TYPES =
            Map.ofEntries(entry(Integer.class, int.class),
                    entry(Long.class, long.class),
                    entry(Boolean.class, boolean.class),
                    entry(Byte.class, byte.class),
                    entry(Short.class, short.class),
                    entry(Float.class, float.class),
                    entry(Double.class, double.class),
                    entry(Character.class, char.class));

    private final Object[] parameterValues;

    public ConstructorParameters(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }
}
