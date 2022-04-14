package ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.objenesis.ObjenesisStd;

import java.lang.invoke.MethodHandles;

import static java.lang.ClassLoader.getSystemClassLoader;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;

public final class ResultMatcherProxyFactory {

    private ResultMatcherProxyFactory() {
        super();
    }

    @SuppressWarnings("unchecked")
    public static <T> T createResultMatcherFactoryProxy(T toProxy) {
        var builder = new ByteBuddy().subclass(toProxy.getClass());
        var interceptor = new ResultMatcherFactoryMethodInterceptor<>(toProxy);

        Class<T> proxyClass;
        try {
            proxyClass = (Class<T>) builder.method(any())
                    .intercept(to(interceptor))
                    .make()
                    .load(getSystemClassLoader(), ClassLoadingStrategy.UsingLookup.of(MethodHandles
                            .privateLookupIn(toProxy.getClass(), MethodHandles.lookup())))
                    .getLoaded();
        } catch (Exception e) {
            throw new ExpectationCreationException(e);
        }

        var objenesis = new ObjenesisStd();
        return objenesis.newInstance(proxyClass);
    }

    private static final class ExpectationCreationException extends RuntimeException {
        private ExpectationCreationException(Throwable t) {
            super(t);
        }
    }
}
