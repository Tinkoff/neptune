package ru.tinkoff.qa.neptune.core.api.proxy;

import ru.tinkoff.qa.neptune.core.api.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.ParameterProvider;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;

@SuppressWarnings("unchecked")
public final class ProxyFactory {

    private ProxyFactory() {
        super();
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or {@link PerformActionStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep}
     *                    and/or {@link PerformActionStep}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param manipulationWithClassToInstantiate is a function which transforms class to be instantiated, e.g bytecode
     *                                           operations by CGLIB or Byte Buddy etc.
     * @param manipulationWithObjectToReturn is a function which transforms created object, e.g creating proxy,
     *                                       changing some attributes etc.
     * @param <T> type of the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or
     * {@link PerformActionStep}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz,
                                   ConstructorParameters constructorParameters,
                                   Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                   Function<T, T> manipulationWithObjectToReturn) {

        checkArgument(PerformActionStep.class.isAssignableFrom(clazz) ||
                GetStep.class.isAssignableFrom(clazz), "Class to substitute should be " +
                "assignable from ru.tinkoff.qa.neptune.core.api.GetStep and/or " +
                "ru.tinkoff.qa.neptune.core.api.PerformActionStep.");

        Class<? extends T> toInstantiate =
                manipulationWithClassToInstantiate.apply(clazz);

        DynamicType.Builder<? extends T> builder = new ByteBuddy().subclass(clazz);

        MethodInterceptor<T> interceptor = new MethodInterceptor<>(clazz,
                (Class<T>) toInstantiate, constructorParameters,
                manipulationWithObjectToReturn);

        Class<? extends T> proxyClass;
        try {
            proxyClass = builder.method(any())
                    .intercept(to(interceptor))
                    .make()
                    .load(InjectionClassLoader.getSystemClassLoader(), ClassLoadingStrategy.UsingLookup.of(MethodHandles
                            .privateLookupIn(clazz, MethodHandles.lookup())))
                    .getLoaded();
        } catch (Throwable e) {
            throw new ProxyCreationFailureException(e.getMessage(), e);
        }

        Objenesis objenesis = new ObjenesisStd();
        return objenesis.newInstance(proxyClass);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or {@link PerformActionStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep}
     *              and/or {@link PerformActionStep}. WARNING!!! It is important!!! Class
     *              to get substituted instance should be annotated by {@link CreateWith}.
     * @param manipulationWithClassToInstantiate is a function which transforms class to be instantiated, e.g bytecode
     *                                            operations by CGLIB or Byte Buddy etc.
     * @param manipulationWithObjectToReturn is a function which transforms created object, e.g creating proxy,
     *                                        changing some attributes etc.
     * @param <T> type of the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or
     * {@link PerformActionStep}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz,
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

        return getProxied(clazz, parameters, manipulationWithClassToInstantiate,
                manipulationWithObjectToReturn);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or {@link PerformActionStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep}
     *                    and/or {@link PerformActionStep}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param <T> type of the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or
     * {@link PerformActionStep}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz,
                                   ConstructorParameters constructorParameters) {
        return getProxied(clazz, constructorParameters, aClass -> aClass, t -> t);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or {@link PerformActionStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep}
     *                    and/or {@link PerformActionStep}. WARNING!!! It is important!!! Class
     *                    to get substituted instance should be annotated by {@link CreateWith}.
     * @param <T> type of the implementor of {@link ru.tinkoff.qa.neptune.core.api.GetStep} and/or
     * {@link PerformActionStep}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz) {
        return getProxied(clazz, aClass -> aClass, t -> t);
    }
}
