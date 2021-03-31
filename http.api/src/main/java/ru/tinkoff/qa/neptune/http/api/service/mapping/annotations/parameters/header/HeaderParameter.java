package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.http.api.properties.date.format.ApiDateFormatProperty.API_DATE_FORMAT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.*;

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
 *         value correctly, see {@link Object#toString()}, arrays and iterables of listed types. Class of a POJO should
 *         extend {@link ru.tinkoff.qa.neptune.http.api.mapping.MappedObject}</li>
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
    boolean required() default false;

    /**
     * @return to explode parameter value or not. Default value is {@code false}.
     * This has an effect when parameter value has type {@link Map} or type of some POJO.
     */
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
                                if (parameter.required()) {
                                    throw new IllegalArgumentException(format("Header '%s' requires value " +
                                                    "that differs from null",
                                            parameter.headerName()));
                                }
                                continue;
                            }

                            map.computeIfAbsent(parameter.headerName(),
                                    s -> new LinkedList<>())
                                    .addAll(getValue(parameter, value));
                        }

                        return map;
                    });
        }

        private static List<String> getValue(HeaderParameter parameter, Object value) {
            var stream = toStream(value);
            if (stream != null) {
                return stream
                        .map(String::valueOf)
                        .collect(toList());
            }

            return ofNullable(objectToMap(value))
                    .map(map -> mapToHeader(map, parameter.explode()))
                    .orElseGet(() -> of(valueOf(value)));
        }

        private static List<String> mapToHeader(Map<?, ?> headerMap, boolean toExpand) {
            var toJoin = new LinkedList<String>();
            headerMap.forEach((o, o2) -> {
                if (!toExpand) {
                    toJoin.add(o + "," + objectToFieldValue(o2));
                } else {
                    toJoin.add(o + "=" + objectToFieldValue(o2));
                }
            });
            return of(join(",", toJoin));
        }

        private static String objectToFieldValue(Object o) {
            return ofNullable(toStream(o))
                    .map(stream -> stream
                            .map(String::valueOf)
                            .collect(joining(",")))
                    .orElseGet(() -> valueOf(o));
        }

        private static String valueOf(Object o) {
            if (o instanceof Date) {
                return ofNullable(API_DATE_FORMAT_PROPERTY.get())
                        .map(simpleDateFormat -> simpleDateFormat.format((Date) o))
                        .orElseGet(o::toString);
            }

            return o.toString();
        }
    }
}
