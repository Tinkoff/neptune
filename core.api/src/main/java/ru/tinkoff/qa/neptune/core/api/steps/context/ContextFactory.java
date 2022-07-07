package ru.tinkoff.qa.neptune.core.api.steps.context;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.objenesis.ObjenesisStd;
import ru.tinkoff.qa.neptune.core.api.steps.proxy.MethodInterceptor;
import ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyCreationFailureException;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.util.Optional.ofNullable;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;

@SuppressWarnings("unchecked")
public final class ContextFactory {

    private static final Map<Class<? extends Context<?>>, Context<?>> knownContexts = new HashMap<>();

    private ContextFactory() {
        super();
    }

    /**
     * Returns a thread-safe instance of {@link Context} if it is created or null otherwise
     *
     * @param cls is a class of an instance to be returned
     * @param <T> is a type of {@link Context}
     * @return a thread-safe instance of {@link Context} if it is created or null otherwise
     */
    public static synchronized <T extends Context<?>> T getCreatedContext(Class<T> cls) {
        checkNotNull(cls);
        return (T) knownContexts.get(cls);
    }

    /**
     * Returns a thread-safe instance of {@link Context}.
     *
     * @param cls is a class of an instance to be returned
     * @param <T> is a type of {@link Context}
     * @return a thread-safe instance of {@link Context}
     */
    public static synchronized <T extends Context<?>> T getCreatedContextOrCreate(Class<T> cls) {
        return ofNullable(getCreatedContext(cls))
                .orElseGet(() -> {
                    var toReturn = getInstance(cls);
                    knownContexts.put(cls, toReturn);
                    return toReturn;
                });
    }

    private static <T extends Context<?>> T getInstance(Class<T> toInstantiate) {
        checkNotNull(toInstantiate);

        var builder = new ByteBuddy().subclass(toInstantiate);
        var interceptor = new MethodInterceptor<>(toInstantiate);

        Class<? extends T> proxyClass;
        try {
            proxyClass = builder.method(any())
                    .intercept(to(interceptor))
                    .make()
                    .load(getSystemClassLoader(), ClassLoadingStrategy.UsingLookup.of(MethodHandles
                            .privateLookupIn(toInstantiate, MethodHandles.lookup())))
                    .getLoaded();
        } catch (Exception e) {
            throw new ProxyCreationFailureException(e.getMessage(), e);
        }

        var objenesis = new ObjenesisStd();
        return objenesis.newInstance(proxyClass);
    }
}
