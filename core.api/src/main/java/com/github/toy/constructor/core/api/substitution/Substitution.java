package com.github.toy.constructor.core.api.substitution;

import java.lang.annotation.Annotation;

public final class Substitution {

    private Substitution() {
        super();
    }

    /**
     * This is the service method which generates a subclass with required properties
     * of the given implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @param multiThreading is the flag which makes instance of the result class usable across
     *           many threads.
     * @param annotations to set to methods that marked by {@link com.github.toy.constructor.core.api.ToBeReported}
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return new sub-class.
     */
    public static <T> Class<T> substitute(Class<T> clazz, boolean multiThreading, Annotation...annotations) {
        return null;
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to instantiate. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param multiThreading is the flag which makes instance of the result class usable across
     *           many threads.
     * @param annotations to set to methods that marked by {@link com.github.toy.constructor.core.api.ToBeReported}
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz, ConstructorParameters constructorParameters,
                                       boolean multiThreading, Annotation...annotations) {
        return null;
    }
}
