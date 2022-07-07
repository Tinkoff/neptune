package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import java.net.URL;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.reflect.Proxy.newProxyInstance;
import static ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBindReader.getRetrofit;
import static ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBindReader.getURL;

/**
 * Creates instances of classes that describe http services.
 */
public final class HttpServiceCreator {

    private HttpServiceCreator() {
        super();
    }

    private static Object createAPI(Class<?> toInstantiate, Supplier<URL> urlSupplier, RetrofitBuilderSupplier retrofit) {
        checkNotNull(toInstantiate);
        checkNotNull(urlSupplier);
        checkNotNull(retrofit);

        return newProxyInstance(getSystemClassLoader(),
                new Class[]{toInstantiate},
                new ServiceProxyHandler(toInstantiate, urlSupplier, retrofit));
    }

    public static Object create(Class<?> toInstantiate, RetrofitBuilderSupplier retrofit) {
        return createAPI(toInstantiate, () -> getURL(toInstantiate), retrofit);
    }

    public static Object create(Class<?> toInstantiate) {
        return createAPI(toInstantiate, () -> getURL(toInstantiate), getRetrofit(toInstantiate));
    }
}
