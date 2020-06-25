package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.ParameterAnnotationTransformer.getFromMethod;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those variable value
 * is used for the forming of a query part of a request URI.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface QueryParameter {

    /**
     * @return a name of http query parameter
     */
    String value();

    /**
     * Util class that reads parameters of a {@link java.lang.reflect.Method} and
     * forms parameters of a query of http request
     */
    final class QueryParameterReader {

        /**
         * Reads parameters of a {@link java.lang.reflect.Method} and parameters of its current invocation
         * and then forms parameters of a query of http request.
         *
         * @param toRead     is a method to be read
         * @param parameters parameters of current invocation of the method
         * @return a map. Keys are names of query parameters and values are lists of values
         * of query parameters
         */
        public static Map<String, List<Object>> readQueryParameters(Method toRead, Object[] parameters) {
            return getFromMethod(toRead,
                    QueryParameter.class,
                    parameters,
                    (ps, params) -> {
                        var map = new HashMap<String, List<Object>>();
                        for (int i = 0; i < ps.length; i++) {
                            var value = params[i];
                            if (value == null) {
                                continue;
                            }

                            var paramName = ps[i].getAnnotation(QueryParameter.class).value();
                            var list = map.computeIfAbsent(paramName,
                                    s -> new LinkedList<>());

                            var cls = value.getClass();
                            if (Iterable.class.isAssignableFrom(cls)) {
                                list.addAll(stream(((Iterable<?>) value).spliterator(), false).collect(toList()));
                            } else if (cls.isArray()) {
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

                                    stream = Arrays.stream(result);
                                } else {
                                    stream = Arrays.stream((Object[]) value);
                                }

                                list.addAll(stream.collect(toList()));
                            } else {
                                list.add(value);
                            }
                        }

                        if (map.size() > 0) {
                            return map;
                        }

                        return null;
                    });
        }
    }
}
