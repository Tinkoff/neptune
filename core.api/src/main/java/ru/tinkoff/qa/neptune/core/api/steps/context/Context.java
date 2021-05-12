package ru.tinkoff.qa.neptune.core.api.steps.context;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.objenesis.ObjenesisStd;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.proxy.MethodInterceptor;
import ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyCreationFailureException;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static ru.tinkoff.qa.neptune.core.api.steps.Absence.absence;
import static ru.tinkoff.qa.neptune.core.api.steps.Presence.presence;

/**
 * /**
 * This class describes something that contains resources for the step performing.
 * These steps are described by {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier} and
 * {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier} generally. "Resources" are opened
 * data base connections, opened browsers etc.
 *
 * @param <THIS> is a type of a class that extends {@link Context}
 */
@SuppressWarnings("unchecked")
public abstract class Context<THIS extends Context<THIS>> {

    /**
     * This is the utility method that helps to create a thread-safe proxy instance with byte-buddy tools.
     * <p>IT IS IMPORTANT</p>
     * It is required for a class to have the default constructor or to be annotated by {@link CreateWith}
     *
     * @param toInstantiate is a class to create instance of
     * @param <T>           is a type that extends {@link Context}
     * @return a new proxy-instance
     */
    protected static <T extends Context<?>> T getInstance(Class<T> toInstantiate) {
        checkNotNull(toInstantiate);

        var createWith = toInstantiate.getAnnotation(CreateWith.class);
        Class<? extends ParameterProvider> provider;

        if (createWith != null) {
            provider = createWith.provider();
        } else {
            provider = ProviderOfEmptyParameters.class;
        }

        Constructor<? extends ParameterProvider> defaultConstructor;
        try {
            defaultConstructor = provider.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(format("%s should have declared default constructor", provider.getName()));
        }

        Object[] parameters;
        try {
            parameters = defaultConstructor.newInstance().provide();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        var builder = new ByteBuddy().subclass(toInstantiate);
        var interceptor = new MethodInterceptor<>(toInstantiate, parameters);

        Class<? extends T> proxyClass;
        try {
            proxyClass = builder.method(any())
                    .intercept(to(interceptor))
                    .make()
                    .load(InjectionClassLoader.getSystemClassLoader(), ClassLoadingStrategy.UsingLookup.of(MethodHandles
                            .privateLookupIn(toInstantiate, MethodHandles.lookup())))
                    .getLoaded();
        } catch (Throwable e) {
            throw new ProxyCreationFailureException(e.getMessage(), e);
        }

        var objenesis = new ObjenesisStd();
        return objenesis.newInstance(proxyClass);
    }

    private static List<Class<? extends Throwable>> ignoredExceptions(Class<? extends Throwable>... toIgnore) {
        List<Class<? extends Throwable>> ignored;
        if (toIgnore != null && toIgnore.length > 0) {
            ignored = asList(toIgnore);
        } else {
            ignored = of();
        }
        return ignored;
    }

    /**
     * Auxiliary method that performs some get-step and then returns a result.
     *
     * @param toGet is a supplier of a get-step to be performed
     * @param <T>   is a type of a resulted value
     * @return a result of the performing of a get-step
     */
    protected final <T> T get(SequentialGetStepSupplier<? super THIS, T, ?, ?, ?> toGet) {
        return toGet.get().apply((THIS) this);
    }

    /**
     * Auxiliary method that performs some action-step.
     *
     * @param toPerform is a supplier of an action to be performed
     * @return self-reference
     */
    protected final THIS perform(SequentialActionSupplier<? super THIS, ?, ?> toPerform) {
        toPerform.get().performAction((THIS) this);
        return (THIS) this;
    }

    /**
     * Checks is some object present. When it is not present then it throws an exception.
     *
     * @param toBePresent is a supplier of a function that retrieves a value
     * @param toIgnore    which exceptions should be ignored during evaluation of {@code toBePresent}
     * @return is desired object present or not
     */
    @SafeVarargs
    protected final boolean presenceOfOrThrow(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBePresent,
                                              Class<? extends Throwable>... toIgnore) {

        return get(presence(toBePresent)
                .addIgnored(ignoredExceptions(toIgnore))
                .throwOnNoResult());
    }

    /**
     * Checks is some object present or not.
     *
     * @param toBePresent is a supplier of a function that retrieves a value
     * @param toIgnore    which exceptions should be ignored during evaluation of {@code toBePresent}
     * @return is desired object present or not
     */
    @SafeVarargs
    protected final boolean presenceOf(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBePresent,
                                       Class<? extends Throwable>... toIgnore) {
        return get(presence(toBePresent)
                .addIgnored(ignoredExceptions(toIgnore)));
    }

    /**
     * Checks is some object absent. When it is present then it throws an exception.
     *
     * @param toBeAbsent is a supplier of a function that retrieves a value
     * @param timeOut    is a time to wait for value is absent. WARNING!!! When {@code toBePresent} has a defined time out
     *                   then it is ignored in favour of a time defined by the method.
     * @return is an object absent or not
     */
    protected final boolean absenceOf(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBeAbsent,
                                      Duration timeOut) {
        return get(absence(toBeAbsent)
                .timeOut(timeOut));
    }

    /**
     * Checks is some object absent or not.
     *
     * @param toBeAbsent is a supplier of a function that retrieves a value
     * @param timeOut    is a time to wait for value is absent. WARNING!!! When {@code toBePresent} has a defined time out
     *                   then it is ignored in favour of a time defined by the method
     * @return is an object absent or not
     */
    protected final boolean absenceOfOrThrow(SequentialGetStepSupplier<? super THIS, ?, ?, ?, ?> toBeAbsent,
                                             Duration timeOut) {
        return get(absence(toBeAbsent)
                .timeOut(timeOut)
                .throwOnNoResult());
    }
}
