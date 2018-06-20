package com.github.toy.constructor.core.api;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.toy.constructor.core.api.SPIUtil.loadSPI;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

@SuppressWarnings("unchecked")
public final class Substitution {

    private Substitution() {
        super();
    }

    private static <T> DynamicType.Builder<T> annotateMethods(DynamicType.Builder<T> builder,
                                                              List<StepAnnotationFactory> factories,
                                                              Predicate<StepAnnotationFactory> stepAnnotationFactoryPredicate,
                                                              Function<StepAnnotationFactory, Annotation> annotationFunction,
                                                              Class<? extends Annotation> annotationToReplace,
                                                              InnerInterceptor innerInterceptor) {
        Stream<StepAnnotationFactory> factoryStream = factories
                .stream()
                .filter(Objects::nonNull);

        List<Annotation> annotations = factoryStream
                .filter(stepAnnotationFactoryPredicate)
                .map(annotationFunction).collect(toList());

        if (annotations.size() > 0) {
            builder = builder.method(isAnnotatedWith(annotationToReplace))
                    .intercept(to(innerInterceptor))
                    .annotateMethod(annotations);
        }
        else {
            builder = builder.method(isAnnotatedWith(annotationToReplace))
                    .intercept(to(innerInterceptor));
        }

        return builder;
    }

    /**
     * This is the service method which generates a subclass
     * of the given implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param factories that generate annotations for {@link PerformStep#perform(Consumer)}, {@link GetStep#get(Function)}
     *                  and {@link GetStep#fireValue(Object)}
     * @return generated sub-class.
     */
    private static <T> Class<? extends T> substitute(Class<T> clazz,
                                                    List<StepAnnotationFactory> factories) throws Exception {
        checkArgument(PerformStep.class.isAssignableFrom(clazz) ||
                GetStep.class.isAssignableFrom(clazz), "Class to substitute should be " +
                "assignable from com.github.toy.constructor.core.api.GetStep and/or " +
                "com.github.toy.constructor.core.api.PerformStep.");

        checkArgument(factories != null,
                "List of annotation factories should not be null");

        DynamicType.Builder<? extends T> builder = new ByteBuddy().subclass(clazz);
        InnerInterceptor interceptor = new InnerInterceptor();

        builder = annotateMethods(builder, factories,
                stepAnnotationFactory -> stepAnnotationFactory.forPerform() != null, StepAnnotationFactory::forPerform,
                StepMarkPerform.class, interceptor);

        builder = annotateMethods(builder, factories,
                stepAnnotationFactory -> stepAnnotationFactory.forGet() != null, StepAnnotationFactory::forGet,
                StepMarkGet.class, interceptor);

        builder = annotateMethods(builder, factories,
                stepAnnotationFactory -> stepAnnotationFactory.forReturn() != null, StepAnnotationFactory::forReturn,
                StepMarkReturn.class, interceptor);

        return builder
                .method(not(isAnnotatedWith(StepMarkPerform.class))
                        .and(not(isAnnotatedWith(StepMarkGet.class)))
                        .and(not(isAnnotatedWith(StepMarkReturn.class))))
                .intercept(to(interceptor))
                .make()
                .load(InjectionClassLoader.getSystemClassLoader(), ClassLoadingStrategy.UsingLookup.of(MethodHandles
                        .privateLookupIn(clazz, MethodHandles.lookup())))
                .getLoaded();
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
     * @param factories that generate annotations for {@link PerformStep#perform(Consumer)}, {@link GetStep#get(Function)}
     *                  and {@link GetStep#fireValue(Object)}
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters,
                                       Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                       Function<T, T> manipulationWithObjectToReturn,
                                       List<StepAnnotationFactory> factories) throws Exception {
        Class<? extends T> toInstantiate =
                manipulationWithClassToInstantiate.apply(substitute(clazz, factories));

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
                                       Function<T, T> manipulationWithObjectToReturn) throws Exception {
        return getSubstituted(clazz, constructorParameters,
                manipulationWithClassToInstantiate, manipulationWithObjectToReturn,
                loadSPI(StepAnnotationFactory.class));
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
     * @param factories that generate annotations for {@link PerformStep#perform(Consumer)}, {@link GetStep#get(Function)}
     *                  and {@link GetStep#fireValue(Object)}
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                       Function<T, T> manipulationWithObjectToReturn,
                                       List<StepAnnotationFactory> factories) throws Exception {
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

        return getSubstituted(clazz, defaultConstructor.newInstance().provide(), manipulationWithClassToInstantiate,
                manipulationWithObjectToReturn, factories);
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
                                       Function<T, T> manipulationWithObjectToReturn) throws Exception {
        return getSubstituted(clazz,
                manipulationWithClassToInstantiate, manipulationWithObjectToReturn,
                loadSPI(StepAnnotationFactory.class));
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param factories that generate annotations for {@link PerformStep#perform(Consumer)}, {@link GetStep#get(Function)}
     *                  and {@link GetStep#fireValue(Object)}
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters,
                                       List<StepAnnotationFactory> factories) throws Exception {
        return getSubstituted(clazz, constructorParameters, aClass -> aClass, t -> t, factories);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters) throws Exception {
        return getSubstituted(clazz, constructorParameters, loadSPI(StepAnnotationFactory.class));
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}. WARNING!!! It is important!!! Class
     *                    to get substituted instance should be annotated by {@link CreateWith}.     *
     * @param factories that generate annotations for {@link PerformStep#perform(Consumer)}, {@link GetStep#get(Function)}
     *                  and {@link GetStep#fireValue(Object)}
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       List<StepAnnotationFactory> factories) throws Exception {
        return getSubstituted(clazz, aClass -> aClass, t -> t, factories);
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
    public static <T> T getSubstituted(Class<T> clazz) throws Exception {
        return getSubstituted(clazz, loadSPI(StepAnnotationFactory.class));
    }
}
