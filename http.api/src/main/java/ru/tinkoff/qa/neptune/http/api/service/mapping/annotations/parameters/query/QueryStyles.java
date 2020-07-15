package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.of;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.objectToMap;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.toStream;

/**
 * Query parameters support the following style values:
 * <ul>
 *     <li>Form - ampersand-separated values, also known as form-style query expansion.</li>
 *     <li>Space delimited - space-separated array values.</li>
 *     <li>Pipe delimited – pipeline-separated array values.</li>
 *     <li>Deep object – a simple way of rendering nested objects using form parameters (applies to objects only).</li>
 * </ul>
 */
public enum QueryStyles {
    /**
     * Form (default) - ampersand-separated values, also known as form-style query expansion.
     */
    FORM {
        @Override
        List<QueryTriplet> arrayValue(Stream<?> valueSource, String varName, boolean explode) {
            return of(new QueryTriplet(varName, explode, valueSource.toArray()));
        }

        @Override
        List<QueryTriplet> mapValue(Map<?, ?> map, String varName, boolean explode) {
            return null;
        }
    },
    /**
     * Space delimited - space-separated array values
     */
    SPACE_DELIMITED,
    /**
     * Pipe delimited – pipeline-separated array values
     */
    PIPE_DELIMITED,
    /**
     * Deep object – a simple way of rendering nested objects using form parameters (applies to objects only)
     */
    DEEP_OBJECT;

    /**
     * Returns a a representation of a query parameter or query part
     *
     * @param queryParameterValue a value to be transformed into query parameter and its values/
     *                            string part of a query
     * @param parameterName       is a name of a query parameter
     * @param explode             to explode value or not
     * @return a list of {@link QueryTriplet}.
     */
    List<QueryTriplet> getQueryParameterValue(Object queryParameterValue, String parameterName, boolean explode) {
        var stream = toStream(queryParameterValue);
        if (stream != null) {
            return arrayValue(stream,
                    parameterName,
                    explode);
        }

        return ofNullable(objectToMap(queryParameterValue))
                .map(map -> mapValue(map, parameterName, explode))
                .orElseGet(() -> of(new QueryTriplet(parameterName, explode, queryParameterValue)));

    }

    List<QueryTriplet> arrayValue(Stream<?> valueSource,
                                  String varName,
                                  boolean explode) {
        throw new UnsupportedOperationException(format("Query parameter %s doesn't support array/collection values " +
                        "due to defined style: %s",
                varName,
                name()));
    }

    List<QueryTriplet> mapValue(Map<?, ?> map,
                                String varName,
                                boolean explode) {
        throw new UnsupportedOperationException(format("Query parameter %s doesn't support object values " +
                        "due to defined style: %s",
                varName,
                name()));
    }
}
