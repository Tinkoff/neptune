package ru.tinkoff.qa.neptune.spring.web.testclient;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.objenesis.ObjenesisStd;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.Function;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.util.Locale.US;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle.getFromResourceBundles;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

abstract class Expectation<T> {

    private final Function<WebTestClient.ResponseSpec, T> assertion;
    final String description;

    private Throwable t;

    Expectation(String description, Function<WebTestClient.ResponseSpec, T> assertion) {
        this.description = description;
        this.assertion = assertion;
    }

    void verify(WebTestClient.ResponseSpec spec) {
        this.t = null;
        try {
            assertion.apply(spec);
        } catch (Throwable t) {
            this.t = t;
        }
    }

    abstract StringBuilder appendDescription(StringBuilder builder);

    @Override
    public String toString() {
        return appendDescription(new StringBuilder(description)).toString();
    }

    Throwable getThrowable() {
        return t;
    }

    static final class SimpleExpectation<T> extends Expectation<T> {

        SimpleExpectation(String description, Function<WebTestClient.ResponseSpec, T> assertion) {
            super(description, assertion);
        }

        @Override
        StringBuilder appendDescription(StringBuilder builder) {
            return builder;
        }
    }

    static final class ExpectationWithSpringAssertion<R, T> extends Expectation<T> {

        String secondDescription;

        private ExpectationWithSpringAssertion(String description, VerifyExpectationWithSpring<R, T> verification) {
            super(description, verification);
            verification.setExpectation(this);
        }

        ExpectationWithSpringAssertion(String description,
                                       Function<WebTestClient.ResponseSpec, R> getObject,
                                       Function<R, T> assertion) {
            this(description, new VerifyExpectationWithSpring<>(getObject, assertion));
        }

        @Override
        StringBuilder appendDescription(StringBuilder builder) {
            return builder.append(". ").append(secondDescription);
        }
    }

    private static final class VerifyExpectationWithSpring<R, T> implements Function<WebTestClient.ResponseSpec, T> {

        private final Function<WebTestClient.ResponseSpec, R> getObject;
        private final Function<R, T> assertion;
        private ExpectationWithSpringAssertion<R, T> expectation;

        private VerifyExpectationWithSpring(Function<WebTestClient.ResponseSpec, R> getObject, Function<R, T> assertion) {
            this.getObject = getObject;
            this.assertion = assertion;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T apply(WebTestClient.ResponseSpec responseSpec) {
            var assertion = getObject.apply(responseSpec);

            var builder = new ByteBuddy().subclass(assertion.getClass());
            var interceptor = new AssertionMethodInterceptor<>(assertion);

            Class<R> proxyClass;
            try {
                proxyClass = (Class<R>) builder.method(any())
                        .intercept(to(interceptor))
                        .make()
                        .load(getSystemClassLoader(), ClassLoadingStrategy.UsingLookup.of(MethodHandles
                                .privateLookupIn(assertion.getClass(), MethodHandles.lookup())))
                        .getLoaded();
            } catch (Exception e) {
                throw new ExpectationCreationException(e);
            }

            var objenesis = new ObjenesisStd();
            var proxy = (R) objenesis.newInstance(proxyClass);
            try {
                return this.assertion.apply(proxy);
            } finally {
                expectation.secondDescription = proxy.toString();
            }
        }

        public VerifyExpectationWithSpring<R, T> setExpectation(ExpectationWithSpringAssertion<R, T> expectation) {
            this.expectation = expectation;
            return this;
        }
    }

    public final static class AssertionMethodInterceptor<T> {

        private static final Locale DEFAULT_LOCALE = US;

        private final T realObject;
        private String description;

        private AssertionMethodInterceptor(T realObject) {
            this.realObject = realObject;
        }

        @SuppressWarnings("unused")
        @RuntimeType
        public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args) throws Throwable {

            if (method.getName().equals("toString") && isEmpty(args)) {
                return description;
            }

            var returnedType = method.getReturnType();
            if (WebTestClient.BodyContentSpec.class.isAssignableFrom(returnedType)
                    || WebTestClient.ResponseSpec.class.isAssignableFrom(returnedType)) {
                description = translate(getFromResourceBundles(DEFAULT_LOCALE, method), method, args);
            }

            return method.invoke(realObject, args);
        }
    }

    private static final class ExpectationCreationException extends RuntimeException {
        private ExpectationCreationException(Throwable t) {
            super(t);
        }
    }
}
