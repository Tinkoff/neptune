package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.objenesis.ObjenesisStd;

import java.lang.invoke.MethodHandles;

import static java.lang.ClassLoader.getSystemClassLoader;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;

final class SearchProxyBuilder {

    private SearchProxyBuilder() {
        super();
    }

    /**
     * Creates a proxy object using the binding of CGLIB and Objenesis
     *
     * @param tClass      is a class of a proxy
     * @param interceptor is an instance of {@link AbstractElementInterceptor}
     * @param <T>         is a type of a proxy
     * @return the resulted object
     */
    static <T> T createProxy(Class<T> tClass, AbstractElementInterceptor interceptor) {
        var builder = new ByteBuddy().subclass(tClass);
        try {
            var proxyClass = builder.method(any())
                    .intercept(to(interceptor))
                    .make()
                    .load(getSystemClassLoader(), ClassLoadingStrategy.UsingLookup.of(MethodHandles
                            .privateLookupIn(tClass, MethodHandles.lookup())))
                    .getLoaded();

            var objenesis = new ObjenesisStd();
            return objenesis.newInstance(proxyClass);
        } catch (Exception e) {
            throw new SearchingProxyCreatorException(e);
        }
    }

    private static final class SearchingProxyCreatorException extends RuntimeException {
        private SearchingProxyCreatorException(Throwable cause) {
            super(cause);
        }
    }
}
