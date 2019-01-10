package ru.tinkoff.qa.neptune.core.api.steps.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.objenesis.ObjenesisStd;
import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.performer.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ParameterProvider;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ActionStepPerformer;
import ru.tinkoff.qa.neptune.core.api.steps.performer.GetStepPerformer;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
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
     * {@link GetStepPerformer} and/or {@link ActionStepPerformer}.
     *
     * @param clazz to substitute. It should be the implementor of {@link GetStepPerformer}
     *                    and/or {@link ActionStepPerformer}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param manipulationWithClassToInstantiate is a function which transforms class to be instantiated, e.g bytecode
     *                                           operations by CGLIB or Byte Buddy etc.
     * @param manipulationWithObjectToReturn is a function which transforms created object, e.g creating proxy,
     *                                       changing some attributes etc.
     * @param <T> type of the implementor of {@link GetStepPerformer} and/or
     * {@link ActionStepPerformer}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz,
                                   ConstructorParameters constructorParameters,
                                   Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                   Function<T, T> manipulationWithObjectToReturn) {

        checkArgument(ActionStepPerformer.class.isAssignableFrom(clazz) ||
                GetStepPerformer.class.isAssignableFrom(clazz), "Class to substitute should be " +
                "assignable from ru.tinkoff.qa.neptune.core.api.steps.performer.GetStepPerformer and/or " +
                "ru.tinkoff.qa.neptune.core.api.steps.performer.ActionStepPerformer.");

        var toInstantiate = manipulationWithClassToInstantiate.apply(clazz);
        var builder = new ByteBuddy().subclass(clazz);
        var interceptor = new MethodInterceptor<>(clazz, (Class<T>) toInstantiate, constructorParameters,
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

        var objenesis = new ObjenesisStd();
        return objenesis.newInstance(proxyClass);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link GetStepPerformer} and/or {@link ActionStepPerformer}.
     *
     * @param clazz to substitute. It should be the implementor of {@link GetStepPerformer}
     *              and/or {@link ActionStepPerformer}. WARNING!!! It is important!!! Class or super-classes
     *              to get substituted instance should be annotated by {@link CreateWith}.
     * @param manipulationWithClassToInstantiate is a function which transforms class to be instantiated, e.g bytecode
     *                                            operations by CGLIB or Byte Buddy etc.
     * @param manipulationWithObjectToReturn is a function which transforms created object, e.g creating proxy,
     *                                        changing some attributes etc.
     * @param <T> type of the implementor of {@link GetStepPerformer} and/or
     * {@link ActionStepPerformer}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz,
                                   Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                   Function<T, T> manipulationWithObjectToReturn) {
        var createWith = ofNullable(clazz.getAnnotation(CreateWith.class))
                .orElseGet(() -> {
                    var superClass = clazz.getSuperclass();
                    while (!superClass.equals(Object.class)) {
                        var createWithA = superClass.getAnnotation(CreateWith.class);
                        if (nonNull(createWithA)) {
                            return createWithA;
                        }
                        superClass = superClass.getSuperclass();
                    }
                    throw new IllegalArgumentException(format("%s or super-classes should be annotated by %s",
                            clazz.getName(), CreateWith.class.getName()));
                });

        var providerClass = createWith.provider();
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
     * {@link GetStepPerformer} and/or {@link ActionStepPerformer}.
     *
     * @param clazz to substitute. It should be the implementor of {@link GetStepPerformer}
     *                    and/or {@link ActionStepPerformer}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param <T> type of the implementor of {@link GetStepPerformer} and/or
     * {@link ActionStepPerformer}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz,
                                   ConstructorParameters constructorParameters) {
        return getProxied(clazz, constructorParameters, aClass -> aClass, t -> t);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link GetStepPerformer} and/or {@link ActionStepPerformer}.
     *
     * @param clazz to substitute. It should be the implementor of {@link GetStepPerformer}
     *                    and/or {@link ActionStepPerformer}. WARNING!!! It is important!!! Class or super-classes
     *                    to get substituted instance should be annotated by {@link CreateWith}.
     * @param <T> type of the implementor of {@link GetStepPerformer} and/or
     * {@link ActionStepPerformer}.
     * @return an instance.
     */
    public static <T> T getProxied(Class<T> clazz) {
        return getProxied(clazz, aClass -> aClass, t -> t);
    }
}
