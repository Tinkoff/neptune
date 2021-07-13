package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import retrofit2.Retrofit;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Objects;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static ru.tinkoff.qa.neptune.core.api.binding.Bind.DefaultBindReader.getBoundTo;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitProperty.DEFAULT_RETROFIT_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitURLProperty.DEFAULT_RETROFIT_URL_PROPERTY;

/**
 * Finds classes that implement {@link URLValuePropertySupplier}
 * and/or {@link RetrofitSupplier} which are bound to interface that describes a service
 */
@SuppressWarnings("unchecked")
public final class RetrofitBindReader {

    @SuppressWarnings("unchecked")
    public static URL getDefaultURL(Class<?> toBindWith) {
        var annotatedElements = getBoundTo(toBindWith);

        return annotatedElements
                .stream()
                .map(annotatedElement -> {
                    if (annotatedElement instanceof Field) {
                        var f = (Field) annotatedElement;
                        f.setAccessible(true);
                        var cls = f.getDeclaringClass();
                        if (cls.isAnonymousClass()) {
                            cls = cls.getSuperclass();
                        }

                        for (var ec : cls.getEnumConstants()) {
                            if (((Enum<?>) ec).name().equals(f.getName())) {
                                return ((URLValuePropertySupplier) ec).get();
                            }
                        }
                        return null;
                    } else if (annotatedElement instanceof Class && URLValuePropertySupplier.class.isAssignableFrom((Class<?>) annotatedElement)) {
                        var c = (Class<URLValuePropertySupplier>) annotatedElement;
                        return stream(c.getDeclaredFields())
                                .filter(f -> isStatic(f.getModifiers()) && (f.getType().equals(c)))
                                .findFirst()
                                .map(f -> {
                                    f.setAccessible(true);
                                    try {
                                        return ((URLValuePropertySupplier) f.get(c)).get();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .orElseGet(() -> {
                                    try {
                                        var constructor = c.getConstructor();
                                        constructor.setAccessible(true);
                                        return (constructor.newInstance()).get();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });

                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(DEFAULT_RETROFIT_URL_PROPERTY);
    }

    public static Retrofit getDefaultRetrofit(Class<?> toBindWith) {
        var annotatedElements = getBoundTo(toBindWith);

        return annotatedElements
                .stream()
                .filter(ae -> (ae instanceof Class && RetrofitSupplier.class.isAssignableFrom((Class<?>) ae)))
                .map(ae -> (Class<RetrofitSupplier>) ae)
                .findFirst()
                .map(cls -> {
                    try {
                        var constructor = cls.getConstructor();
                        constructor.setAccessible(true);
                        return constructor.newInstance().get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseGet(DEFAULT_RETROFIT_PROPERTY);
    }
}
