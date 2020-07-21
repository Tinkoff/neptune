package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
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
     * Reads an annotation and returns information about a {@link Parameter}.
     * This is information about necessity of the parameter. When the parameter is required
     * then {@code null} value is not allowed.
     * <p>
     * It searches for a method annotated by {@link Required}.
     * {@code null} value also may be returned when there is no method annotated by {@link Required}
     *
     * @param a is an annotation to read
     * @return {@code true} or {@code false} or {@code null} when there is no method annotated by {@link Required}.
     */
    public static Boolean isRequired(Annotation a) {
        return returnBoolean(a, Required.class);
    }

    /**
     * Reads an annotation and returns information about a {@link Parameter}.
     * This is information about necessity to expand value of the parameter.
     * <p>
     * It searches for a method annotated by {@link ToExpand}.
     * {@code null} value also may be returned when there is no method annotated by {@link ToExpand}
     *
     * @param a is an annotation to read
     * @return {@code true} or {@code false} or {@code null} when there is no method annotated by {@link ToExpand}.
     */
    public static Boolean toExpandValue(Annotation a) {
        return returnBoolean(a, ToExpand.class);
    }

    /**
     * Reads an annotation and returns information about a {@link Parameter}.
     * This is information about necessity to keep reserved characters as is.
     * <p>
     * It searches for a method annotated by {@link AllowReserved}.
     * {@code null} value also may be returned when there is no method annotated by {@link AllowReserved}
     *
     * @param a is an annotation to read
     * @return {@code true} or {@code false} or {@code null} when there is no method annotated by {@link AllowReserved}.
     */
    public static Boolean toAllowReserved(Annotation a) {
        return returnBoolean(a, AllowReserved.class);
    }

    private static <T extends Annotation> Boolean returnBoolean(Annotation a, Class<T> toFind) {
        checkNotNull(a);
        checkNotNull(toFind);

        var method = stream(a.annotationType().getDeclaredMethods())
                .filter(m -> !isStatic(m.getModifiers())
                        && m.getReturnType().equals(boolean.class)
                        && m.getAnnotation(toFind) != null
                        && m.getParameters().length == 0)
                .findFirst()
                .orElse(null);

        return ofNullable(method)
                .map(m -> {
                    try {
                        return (Boolean) m.invoke(a);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(null);
    }

    /**
     * Transforms object to {@link Map}
     *
     * @param value object to be transformed to map
     * @return a map when an object is {@link Map} or a class or subclass of the given
     * object is annotated by {@link MethodParameter}. It returns {@code null} otherwise.
     */
    public static Map<?, ?> objectToMap(Object value) {
        var cls = value.getClass();

        if (Map.class.isAssignableFrom(cls)) {
            return (Map<?, ?>) value;
        }

        if (isAMethodParameter(cls)) {
            var result = new LinkedHashMap<String, Object>();
            while (!cls.equals(Object.class)) {
                var fs = cls.getDeclaredFields();
                for (var f : fs) {
                    f.setAccessible(true);
                    try {
                        var v = f.get(value);
                        ofNullable(v).ifPresent(o -> {
                            var clazz = o.getClass();
                            Object val;
                            if (clazz.isArray()) {
                                val = toStream(o).collect(toList());
                            } else {
                                val = o;
                            }
                            result.put(ofNullable(f.getAnnotation(ParameterFieldName.class))
                                            .map(ParameterFieldName::value)
                                            .orElseGet(f::getName),
                                    val);
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                cls = cls.getSuperclass();
            }
            return result;
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

    /**
     * Checks is a class represents a parameter (header, query parameter or path variable)
     * of http request or not.
     *
     * @param cls is a class to check
     * @return is a class represents method parameter or not
     */
    public static boolean isAMethodParameter(Class<?> cls) {
        var superCls = cls;
        var isMethodParameter = superCls.getAnnotation(MethodParameter.class) != null;

        while (!isMethodParameter && !superCls.equals(Object.class)) {
            superCls = superCls.getSuperclass();
            isMethodParameter = superCls.getAnnotation(MethodParameter.class) != null;
        }

        return isMethodParameter;
    }
}
