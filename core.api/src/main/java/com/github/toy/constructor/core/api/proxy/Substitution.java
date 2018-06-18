package com.github.toy.constructor.core.api.proxy;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.StepMark;
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
import java.util.*;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

public final class Substitution {

    private Substitution() {
        super();
    }

    private static List<Logger> loadSPI(List<Logger> additional) {
        List<Logger> loggers = ServiceLoader.load(Logger.class)
                .stream()
                .map(ServiceLoader.Provider::get).collect(toList());

        List<? extends Class<? extends Logger>> loggerClasses =
                loggers.stream().map(Logger::getClass).collect(toList());

        loggers.addAll(additional
                .stream()
                .filter(logger -> !loggerClasses.contains(ofNullable(logger).map(Logger::getClass).orElse(null)))
                .collect(toList()));
        return loggers;
    }

    /**
     * This is the service method which generates a subclass
     * of the given implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param annotations to set to methods that marked by {@link StepMark}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @return generated sub-class.
     */
    private static <T> Class<? extends T> substitute(Class<T> clazz,
                                                     List<Logger> loggers,
                                                    Annotation...annotations) throws Exception {
        checkArgument(PerformStep.class.isAssignableFrom(clazz) ||
                GetStep.class.isAssignableFrom(clazz), "Class to substitute should be " +
                "assignable from com.github.toy.constructor.core.api.GetStep and/or " +
                "com.github.toy.constructor.core.api.PerformStep.");
        DynamicType.Builder<? extends T> builder = new ByteBuddy().subclass(clazz);

        InnerInterceptor interceptor = new InnerInterceptor(loadSPI(loggers));
        return builder.method(isAnnotatedWith(StepMark.class))
                .intercept(to(interceptor))
                .annotateMethod(annotations)
                .method(not(isAnnotatedWith(StepMark.class)))
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
     *                                            operations by CGLIB or Byte Buddy etc.
     * @param manipulationWithObjectToReturn is a function which transforms created object, e.g creating proxy,
     *                                        changing some attributes etc.
     * @param loggers list of custom loggers. see {@link Logger}
     * @param annotations to set to methods that marked by {@link StepMark}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters,
                                       Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                       Function<T, T> manipulationWithObjectToReturn,
                                       List<Logger> loggers,
                                       Annotation...annotations) throws Exception {
        Class<? extends T> toInstantiate =
                manipulationWithClassToInstantiate.apply(substitute(clazz, loggers, annotations));

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
     * @param loggers list of custom loggers. see {@link Logger}
     * @param annotations to set to methods that marked by {@link StepMark}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       Function<Class<? extends T>, Class<? extends T>> manipulationWithClassToInstantiate,
                                       Function<T, T> manipulationWithObjectToReturn,
                                       List<Logger> loggers,
                                       Annotation...annotations) throws Exception {
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
                manipulationWithObjectToReturn, loggers, annotations);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param loggers list of custom loggers. see {@link Logger}
     * @param annotations to set to methods that marked by {@link StepMark}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters,
                                       List<Logger> loggers,
                                       Annotation...annotations) throws Exception {
        return getSubstituted(clazz, constructorParameters, aClass -> aClass, t -> t, loggers, annotations);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}. WARNING!!! It is important!!! Class
     *                    to get substituted instance should be annotated by {@link CreateWith}.
     *
     * @param loggers list of custom loggers. see {@link Logger}
     * @param annotations to set to methods that marked by {@link StepMark}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       List<Logger> loggers,
                                       Annotation...annotations) throws Exception {
        return getSubstituted(clazz, aClass -> aClass, t -> t, loggers, annotations);
    }


    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param constructorParameters is a POJO with wrapped parameters of required constructor.
     * @param annotations to set to methods that marked by {@link StepMark}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       ConstructorParameters constructorParameters,
                                       Annotation...annotations) throws Exception {
        return getSubstituted(clazz, constructorParameters, List.of(), annotations);
    }

    /**
     * This is the service method which creates an instance of the given implementor of
     * {@link com.github.toy.constructor.core.api.GetStep} and/or {@link com.github.toy.constructor.core.api.PerformStep}.
     *
     * @param clazz to substitute. It should be the implementor of {@link com.github.toy.constructor.core.api.GetStep}
     *                    and/or {@link com.github.toy.constructor.core.api.PerformStep}. WARNING!!! It is important!!! Class
     *                    to get substituted instance should be annotated by {@link CreateWith}.
     * @param annotations to set to methods that marked by {@link StepMark}.
     *                    These annotations should describe steps. Their description should be like {@value {0}} or
     *                    some string convenient to the formatting with a single parameter.
     * @param <T> type of the implementor of {@link com.github.toy.constructor.core.api.GetStep} and/or
     * {@link com.github.toy.constructor.core.api.PerformStep}.
     * @return an instance.
     */
    public static <T> T getSubstituted(Class<T> clazz,
                                       Annotation...annotations) throws Exception {
        return getSubstituted(clazz, List.of(), annotations);
    }
}
