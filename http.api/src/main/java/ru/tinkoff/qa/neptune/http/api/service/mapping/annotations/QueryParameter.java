package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
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
                            map.computeIfAbsent(paramName,
                                    s -> new LinkedList<>())
                                    .add(value);
                        }

                        if (map.size() > 0) {
                            return map;
                        }

                        return null;
                    });
        }
    }
}
