package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods;

import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

/**
 * Marks methods of {@link HttpAPI}. It defines http constant headers (such as {@code 'Content-Type'})
 * of resulted http request. {@link HeaderParameter} is recommended to use when value is variable.
 */
@Retention(RUNTIME)
@Repeatable(Headers.class)
@Target(METHOD)
public @interface Header {

    /**
     * @return name of a header
     */
    String name();

    /**
     * @return values of a header
     */
    String[] headerValues();

    /**
     * Util class that reads a {@link java.lang.reflect.Method} and
     * forms map of headers of http request and their values.
     */
    final class HeaderReader {

        /**
         * Reads a {@link java.lang.reflect.Method} and
         * forms map of headers of http request and their values.
         *
         * @param toRead is a method to be read
         * @return a map. Keys are names of headers of http request and values are lists of values
         * of these headers.
         */
        public static Map<String, List<String>> readHeaders(Method toRead) {
            var headers = toRead.getAnnotationsByType(Header.class);
            var map = new HashMap<String, List<String>>();

            stream(headers).forEach(header -> map
                    .computeIfAbsent(header.name(), s -> new LinkedList<>())
                    .addAll(asList(header.headerValues())));

            if (!map.isEmpty()) {
                return map;
            }

            return null;
        }
    }
}
