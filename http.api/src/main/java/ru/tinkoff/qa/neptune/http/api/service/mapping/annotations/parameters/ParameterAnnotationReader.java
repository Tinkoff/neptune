package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.add;

/**
 * Util that transforms annotations of parameters of a {@link java.lang.reflect.Method} into
 * required objects.
 */
public final class ParameterAnnotationReader {

    private ParameterAnnotationReader() {
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
}
