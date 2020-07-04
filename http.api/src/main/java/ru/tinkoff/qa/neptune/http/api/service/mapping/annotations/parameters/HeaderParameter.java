package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.MethodParameter.MethodParameterDetector.isAMethodParameter;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterAnnotationReader.*;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those variable value
 * forms a header of a request URI.
 * <p>
 * Required types:
 *     <ul>
 *         <li>primitive</li>
 *         <li>primitive wrappers</li>
 *         <li>{@link String}</li>
 *         <li>Any object whose string representation may be read as header value correctly. See {@link Object#toString()}</li>
 *         <li>Arrays of types described above</li>
 *         <li>Iterable of described above</li>
 *         <li>Maps of keys and values of types described above</li>
 *         <li>A POJO whose class has fields of following types: primitive types, primitive wrappers,
 *         {@link String}, any type of an object whose string representation may be read as header
 *         value correctly, see {@link Object#toString()}. Class of a POJO should be annotated
 *         by {@link MethodParameter}</li>
 *     </ul>
 * </p>
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface HeaderParameter {

    /**
     * @return name of a header
     */
    String headerName();

    /**
     * @return is parameter required or not. Default value is {@code false}.
     * It allows or doesn't allow {@code null} values of a parameter on a method
     * invocation.
     */
    @Required
    boolean required() default false;

    /**
     * @return @return to explode parameter value or not. Default value is {@code false}.
     * This has an effect when parameter value has type {@link Map} or type of some POJO.
     */
    @ToExpand
    boolean explode() default false;

    /**
     * Util class that reads parameters of a {@link java.lang.reflect.Method} and
     * adds additional headers to resulted http request.
     */
    final class HeaderParameterReader {

        private HeaderParameterReader() {
            super();
        }

        /**
         * Reads parameters of a {@link java.lang.reflect.Method} and parameters of its current invocation
         * and then forms a map where keys are names of http headers and values are values of headers.
         *
         * @param toRead     is a method to be read
         * @param parameters parameters of current invocation of the method
         * @return a map where keys are names of http headers and values are values of headers
         */
        public static Map<String, List<String>> readHeaderParameters(Method toRead, Object[] parameters) {
            return getFromMethod(toRead,
                    HeaderParameter.class,
                    parameters,
                    (ps, params) -> {
                        var map = new HashMap<String, List<String>>();

                        for (int i = 0; i < ps.length; i++) {
                            var parameter = ps[i].getAnnotation(HeaderParameter.class);
                            var value = params[i];

                            if (value == null) {
                                if (isRequired(parameter)) {
                                    throw new UnsupportedOperationException(format("Value of header %s is defined as required, but " +
                                            "given value is null", parameter.headerName()));
                                }
                                continue;
                            }

                            map.computeIfAbsent(parameter.headerName(),
                                    s -> new LinkedList<>())
                                    .add(getValue(parameter, value));
                        }

                        return map;
                    });
        }

        private static String getValue(HeaderParameter parameter, Object value) {
            var cls = value.getClass();

            if (cls.isPrimitive() || isPrimitiveOrWrapper(cls)) {
                return valueOf(value);
            }

            if (String.class.isAssignableFrom(cls)) {
                return (String) value;
            }

            if (cls.isArray()) {
                Stream<?> stream;
                Object[] result;
                if (cls.getComponentType().isPrimitive()) {
                    if (byte[].class.equals(cls)) {
                        var byteArray = (byte[]) value;
                        result = new Byte[byteArray.length];
                        for (var j = 0; j < byteArray.length; j++) {
                            result[j] = byteArray[j];
                        }
                    } else if (short[].class.equals(cls)) {
                        var shortArray = (short[]) value;
                        result = new Short[shortArray.length];
                        for (var j = 0; j < shortArray.length; j++) {
                            result[j] = shortArray[j];
                        }
                    } else if (int[].class.equals(cls)) {
                        var intArray = (int[]) value;
                        result = new Integer[intArray.length];
                        for (var j = 0; j < intArray.length; j++) {
                            result[j] = intArray[j];
                        }
                    } else if (long[].class.equals(cls)) {
                        var longArray = (long[]) value;
                        result = new Long[longArray.length];
                        for (var j = 0; j < longArray.length; j++) {
                            result[j] = longArray[j];
                        }
                    } else if (float[].class.equals(cls)) {
                        var floatArray = (float[]) value;
                        result = new Float[floatArray.length];
                        for (var j = 0; j < floatArray.length; j++) {
                            result[j] = floatArray[j];
                        }
                    } else if (double[].class.equals(cls)) {
                        var doubleArray = (double[]) value;
                        result = new Double[doubleArray.length];
                        for (var j = 0; j < doubleArray.length; j++) {
                            result[j] = doubleArray[j];
                        }
                    } else if (boolean[].class.equals(cls)) {
                        var booleanArray = (boolean[]) value;
                        result = new Boolean[booleanArray.length];
                        for (var j = 0; j < booleanArray.length; j++) {
                            result[j] = booleanArray[j];
                        }
                    } else {
                        var charArray = (char[]) value;
                        result = new Character[charArray.length];
                        for (var j = 0; j < charArray.length; j++) {
                            result[j] = charArray[j];
                        }
                    }

                    stream = stream(result);
                } else {
                    stream = stream((Object[]) value);
                }

                return stream
                        .map(String::valueOf)
                        .collect(joining(","));
            }

            if (Iterable.class.isAssignableFrom(cls)) {
                return StreamSupport.stream(((Iterable<?>) value).spliterator(), false)
                        .map(String::valueOf)
                        .collect(joining(","));
            }

            if (Map.class.isAssignableFrom(cls)) {
                return mapToHeader((Map<?, ?>) value, toExpandValue(parameter));
            }

            if (isAMethodParameter(cls)) {
                return mapToHeader(objectToMap(value), toExpandValue(parameter));
            }

            return valueOf(value);
        }

        private static String mapToHeader(Map<?, ?> headerMap, boolean toExpand) {
            var toJoin = new LinkedList<String>();
            headerMap.forEach((o, o2) -> {
                if (!toExpand) {
                    toJoin.add(o + "," + o2);
                } else {
                    toJoin.add(o + "=" + o2);
                }
            });
            return join(",", toJoin);
        }

        private static Map<String, ?> objectToMap(Object value) {
            var cls = value.getClass();
            var result = new LinkedHashMap<String, Object>();
            while (!cls.equals(Object.class)) {
                var fs = cls.getDeclaredFields();
                stream(fs).forEach(f -> {
                    f.setAccessible(true);
                    try {
                        var v = f.get(value);
                        ofNullable(v).ifPresent(o -> result.put(f.getName(), v));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                cls = cls.getSuperclass();
            }
            return result;
        }
    }
}
