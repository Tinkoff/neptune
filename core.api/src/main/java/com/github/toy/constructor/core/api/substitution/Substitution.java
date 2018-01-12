package com.github.toy.constructor.core.api.substitution;

import com.github.toy.constructor.core.api.ToBeReported;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static java.util.Map.entry;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.*;

public final class Substitution {

    private static final Map<Class<?>, Class<?>> FOR_USED_SIMPLE_TYPES =
            Map.ofEntries(entry(Integer.class, int.class),
                    entry(Long.class, long.class),
                    entry(Boolean.class, boolean.class),
                    entry(Byte.class, byte.class),
                    entry(Short.class, short.class),
                    entry(Float.class, float.class),
                    entry(Double.class, double.class),
                    entry(Character.class, char.class));

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
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param annotations to set to methods that marked by {@link com.github.toy.constructor.core.api.ToBeReported}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @return new sub-class.
     */
    public static <T> Class<T> substitute(Class<T> clazz, ConstructorParameters constructorParameters,
                                      Annotation...annotations) {
        DynamicType.Builder<T> builder = new ByteBuddy().subclass(clazz);
        Constructor<T> c = findSuitableConstructor(clazz, constructorParameters.getParameterValues());
        c.setAccessible(true);
        builder.define(c);

        return (Class<T>) builder.method(isAnnotatedWith(ToBeReported.class))
                .intercept(to(new InnerInterceptor(clazz, constructorParameters)))
                .annotateMethod(annotations)
                .method(not(isAnnotatedWith(ToBeReported.class)))
                .intercept(to(new InnerInterceptor(clazz, constructorParameters)))
                .make().load(Substitution.class.getClassLoader())
                .getLoaded();
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to instantiate. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param annotations to set to methods that marked by {@link com.github.toy.constructor.core.api.ToBeReported}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz, ConstructorParameters constructorParameters,
                                       Annotation...annotations) {
        Constructor<T> c =
                findSuitableConstructor(substitute(clazz, constructorParameters, annotations), constructorParameters);
        try {
            return c.newInstance(constructorParameters.getParameterValues());
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> Constructor<T> findSuitableConstructor(Class<T> clazz, Object...params) {
        //TODO needs for implementation
        return null;
    }
}
