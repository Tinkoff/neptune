package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query;

import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.Required;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ToExpand;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.*;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryStyles.FORM;

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
    String name();

    /**
     * @return is parameter required or not. Default value is {@code true}.
     * It allows or doesn't allow {@code null} values of a parameter on a method
     * invocation.
     */
    @Required
    boolean required() default true;

    /**
     * @return @return to explode parameter value or not. Default value is {@code true}.
     * This has an effect when parameter value has type {@link Map} or type of some POJO.
     */
    @ToExpand
    boolean explode() default true;

    /**
     * @return style of a query parameter. Default is {@link QueryStyles#FORM}
     */
    QueryStyles style() default FORM;

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
         * @return a list of {@link Query}.
         */
        public static List<Query> readQueryParameters(Method toRead, Object[] parameters) {
            return getFromMethod(toRead,
                    QueryParameter.class,
                    parameters,
                    (ps, params) -> {
                        var resultList = new LinkedList<Query>();

                        for (int i = 0; i < ps.length; i++) {
                            var queryParameter = ps[i].getAnnotation(QueryParameter.class);
                            var value = params[i];
                            if (value == null) {
                                if (isRequired(queryParameter)) {
                                    throw new IllegalArgumentException(format("Query parameter '%s' requires value " +
                                                    "that differs from null",
                                            queryParameter.name()));
                                }
                                continue;
                            }

                            var queryPart = queryParameter.style().getQueryParameterValue(value,
                                    queryParameter.name(),
                                    toExpandValue(queryParameter));
                            resultList.addAll(queryPart);
                        }

                        if (resultList.size() > 0) {
                            return resultList;
                        }

                        return null;
                    });
        }
    }
}
