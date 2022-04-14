package ru.tinkoff.qa.neptune.http.api.service.mapping;

import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.binding.Bind.DefaultBindReader.getBoundTo;

/**
 * Finds classes that implement {@link URLValuePropertySupplier}
 * and/or {@link RequestTuner} which are bound to subclasses of {@link HttpAPI}
 */
@SuppressWarnings("unchecked")
public final class HttpServiceBindReader {

    @SuppressWarnings("unchecked")
    public static URLValuePropertySupplier getDefaultURLProperty(Class<? extends HttpAPI<?>> toBindWith) {
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
                                return (URLValuePropertySupplier) ec;
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
                                        return (URLValuePropertySupplier) f.get(c);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .orElseGet(() -> {
                                    try {
                                        var constructor = c.getConstructor();
                                        constructor.setAccessible(true);
                                        return constructor.newInstance();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });

                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static List<Class<RequestTuner>> getRequestTuners(Class<? extends HttpAPI<?>> toBindWith) {
        var annotatedElements = getBoundTo(toBindWith);

        return annotatedElements
                .stream()
                .filter(ae -> (ae instanceof Class && RequestTuner.class.isAssignableFrom((Class<?>) ae)))
                .map(ae -> (Class<RequestTuner>) ae)
                .collect(toList());
    }
}
