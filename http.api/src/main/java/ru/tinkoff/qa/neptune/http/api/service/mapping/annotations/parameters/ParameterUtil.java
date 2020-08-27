package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.ArrayUtils.toObject;

/**
 * Util that transforms annotations of parameters of a {@link java.lang.reflect.Method} into
 * required objects.
 */
public final class ParameterUtil {

    private ParameterUtil() {
        super();
    }

    /**
     * Returns desired object using signature of an invoked {@link Method}, values of parameters of current invocation of
     * the method and type of annotation that should mark parameters of the method.
     *
     * @param m                is an invoked method of {@link HttpAPI}
     * @param annotationToFind is type of annotation that is expected to mark any parameters of the method
     * @param invocationParams is an array of parameters of current invocation of the method
     * @param howToGet         is a bi-function that describes how to get desired result. Here {@code Parameter[]}
     *                         is an array of parameters annotated by {@code annotationToFind} and {@code Object[]}
     *                         is an array of corresponding objects taken from the {@code invocationParams}
     * @param <T>              is a type of resulted objects
     * @param <R>              is type of annotation that is expected to mark parameters of the method
     * @return {@code T} if there are parameters annotated by {@code annotationToFind}.
     * {@code null} is returned otherwise.
     */
    public static <T, R> T getFromMethod(Method m, Class<R> annotationToFind,
                                         Object[] invocationParams,
                                         BiFunction<Parameter[], Object[], T> howToGet) {
        var params = new Parameter[]{};
        var methodParams = m.getParameters();
        var values = new Object[]{};

        var annotations = m.getParameterAnnotations();
        for (var i = 0; i < annotations.length; i++) {
            var annotationsOfParam = annotations[i];
            if (stream(annotationsOfParam)
                    .anyMatch(a -> nonNull(a) && annotationToFind.isAssignableFrom(a.annotationType()))) {
                params = add(params, methodParams[i]);
                values = add(values, invocationParams[i]);
            }
        }

        if (params.length > 0) {
            return howToGet.apply(params, values);
        }
        return null;
    }

    /**
     * Transforms object to {@link Map}
     *
     * @param value object to be transformed to map
     * @return a map when an object is {@link Map} or a class is a subclass of {@link MappedObject}.
     */
    public static Map<?, ?> objectToMap(Object value) {
        var cls = value.getClass();

        if (Map.class.isAssignableFrom(cls)) {
            return (Map<?, ?>) value;
        }

        if (MappedObject.class.isAssignableFrom(cls)) {
            return ((MappedObject) value).toMap();
        }

        return null;
    }

    /**
     * Returns an object transformed into {@link Stream}.
     *
     * @param value to be transformed into stream.
     * @return a stream when the given value is array or iterable.
     * It returns {@code null} otherwise.
     */
    public static Stream<?> toStream(Object value) {
        var cls = value.getClass();
        Stream<?> stream = null;

        if (cls.isArray()) {
            Object[] result;
            if (cls.getComponentType().isPrimitive()) {
                if (byte[].class.equals(cls)) {
                    result = toObject((byte[]) value);
                } else if (short[].class.equals(cls)) {
                    result = toObject((short[]) value);
                } else if (int[].class.equals(cls)) {
                    result = toObject((int[]) value);
                } else if (long[].class.equals(cls)) {
                    result = toObject((long[]) value);
                } else if (float[].class.equals(cls)) {
                    result = toObject((float[]) value);
                } else if (double[].class.equals(cls)) {
                    result = toObject((double[]) value);
                } else if (boolean[].class.equals(cls)) {
                    result = toObject((boolean[]) value);
                } else {
                    result = toObject((char[]) value);
                }

                stream = stream(result);
            } else {
                stream = stream((Object[]) value);
            }
        } else if (Iterable.class.isAssignableFrom(cls)) {
            stream = StreamSupport.stream(((Iterable<?>) value).spliterator(), false);
        }

        return ofNullable(stream)
                .map(s -> s.filter(Objects::nonNull))
                .orElse(null);
    }
}
