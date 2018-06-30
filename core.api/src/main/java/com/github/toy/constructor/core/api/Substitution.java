package com.github.toy.constructor.core.api;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

@SuppressWarnings("unchecked")
public final class Substitution {

    private Substitution() {
        super();
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param manipulationWithClassToInstantiate is a function which transforms class to be instantiated, e.g bytecode
     *                                           operations by CGLIB or Byte Buddy etc.
     * @param manipulationWithObjectToReturn is a function which transforms created object, e.g creating proxy,
     *                                       changing some attributes etc.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters,
                                       Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                       Function<T, T> manipulationWithObjectToReturn) {

        checkArgument(PerformStep.class.isAssignableFrom(clazz) ||
                GetStep.class.isAssignableFrom(clazz), "Class to substitute should be " +
                "assignable from com.github.toy.constructor.core.api.GetStep and/or " +
                "com.github.toy.constructor.core.api.PerformStep.");

        Class<? extends T> toInstantiate =
                manipulationWithClassToInstantiate.apply(clazz);

        Enhancer enhancer = new Enhancer();
        OuterMethodInterceptor<T> interceptor =
                new OuterMethodInterceptor<>(clazz, (Class<T>) toInstantiate, constructorParameters, manipulationWithObjectToReturn);

        enhancer.setUseCache(false);
        enhancer.setCallbackType(OuterMethodInterceptor.class);
        enhancer.setSuperclass(clazz);
        Class<?> proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(clazz.getClassLoader());

        Objenesis objenesis = new ObjenesisStd();
        return (T) objenesis.newInstance(proxyClass);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *              and/or {@link com.github.toy.constructor.core.api.PerformStep}. WARNING!!! It is important!!! Class
     *              to get substituted instance should be annotated by {@link CreateWith}.
     * @param manipulationWithClassToInstantiate is a function which transforms class to be instantiated, e.g bytecode
     *                                            operations by CGLIB or Byte Buddy etc.
     * @param manipulationWithObjectToReturn is a function which transforms created object, e.g creating proxy,
     *                                        changing some attributes etc.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                       Function<T, T> manipulationWithObjectToReturn) {
        CreateWith createWith = ofNullable(clazz.getAnnotation(CreateWith.class))
                .orElseThrow(() -> new IllegalArgumentException(format("%s should be annotated by %s",
                        clazz.getName(), CreateWith.class.getName())));
        Class<? extends ParameterProvider> providerClass = createWith.provider();
        Constructor<? extends ParameterProvider> defaultConstructor;
        try {
            defaultConstructor = providerClass.getDeclaredConstructor();
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(format("%s should have declared default constructor",
                    clazz.getName()));
        }
        defaultConstructor.setAccessible(true);

        ConstructorParameters parameters;
        try {
            parameters =
                    defaultConstructor.newInstance().provide();
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return getSubstituted(clazz, parameters, manipulationWithClassToInstantiate,
                manipulationWithObjectToReturn);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters) {
        return getSubstituted(clazz, constructorParameters, aClass -> aClass, t -> t);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}. WARNING!!! It is important!!! Class
     *                    to get substituted instance should be annotated by {@link CreateWith}.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz) {
        return getSubstituted(clazz, aClass -> aClass, t -> t);
    }
}
