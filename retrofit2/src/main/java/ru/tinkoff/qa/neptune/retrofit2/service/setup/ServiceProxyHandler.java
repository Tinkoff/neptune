package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

final class ServiceProxyHandler implements InvocationHandler {

    private static final ThreadLocal<Map<ServiceProxyHandler, Object>> instantiated = new ThreadLocal<>();

    private final Class<?> toInstantiate;
    private final Supplier<URL> urlSupplier;
    private final RetrofitBuilderSupplier retrofitBuilderSupplier;

    public ServiceProxyHandler(Class<?> toInstantiate, Supplier<URL> urlSupplier, RetrofitBuilderSupplier retrofitBuilderSupplier) {
        this.toInstantiate = toInstantiate;
        this.urlSupplier = urlSupplier;
        this.retrofitBuilderSupplier = retrofitBuilderSupplier;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var baseURL = urlSupplier.get();
        checkState(nonNull(baseURL), "Base URL is not defined");
        method.setAccessible(true);

        var map = ofNullable(instantiated.get())
                .orElseGet(() -> {
                    var toRemember = new HashMap<ServiceProxyHandler, Object>();
                    instantiated.set(toRemember);
                    return toRemember;
                });

        var service = map.computeIfAbsent(this,
                ih -> retrofitBuilderSupplier.get().baseUrl(baseURL).build().create(toInstantiate));

        return method.invoke(service, args);
    }
}
