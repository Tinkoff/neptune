package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations;

import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.ParameterAnnotationTransformer.getFromMethod;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those variable value
 * forms a path of a request URI.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface PathParameter {

    /**
     * @return variable part of a path of a request URI
     */
    String value();

    /**
     * Util class that reads parameters of a {@link java.lang.reflect.Method} and
     * forms map of variables and their values to insert it to {@link URI} as path.
     */
    final class PathParameterReader {

        /**
         * Reads parameters of a {@link java.lang.reflect.Method} and parameters of its current invocation
         * and then creates URI-path to requested endpoint.
         *
         * @param toRead     is a method to be read
         * @param parameters parameters of current invocation of the method
         * @return string URI-path to requested endpoint.
         */
        public static String readPathParameters(Method toRead, Object[] parameters) {
            var rawPath = ofNullable(toRead.getAnnotation(URIPath.class))
                    .map(uriPath -> {
                        var path = uriPath.value();
                        if (isBlank(path)) {
                            throw new UnsupportedOperationException(format("A blank value of uri-path is defined for the %s",
                                    toRead));
                        }
                        return path;
                    })
                    .orElse(null);

            if (rawPath == null) {
                return null;
            }

            return ofNullable(getFromMethod(toRead,
                    PathParameter.class,
                    parameters,
                    (ps, params) -> {
                        var map = new HashMap<String, String>();
                        for (int i = 0; i < ps.length; i++) {
                            var pathVariable = ps[i].getAnnotation(PathParameter.class).value();
                            if (map.get(pathVariable) != null) {
                                throw new UnsupportedOperationException("Path variable '%s' is defined more than once. " +
                                        "This is not supported");
                            }
                            map.put(pathVariable, encode(valueOf(params[i]), UTF_8));
                        }

                        var result = rawPath;
                        for (var e : map.entrySet()) {
                            var s = e.getKey();
                            var s2 = e.getValue();

                            var pattern = "{" + s + "}";
                            if (s.indexOf(result, result.indexOf(pattern) + pattern.length()) > -1) {
                                throw new IllegalArgumentException(format("Path variable %s is defined more than once " +
                                                "in %s of %s",
                                        pattern,
                                        URIPath.class.getName(),
                                        toRead));
                            }
                            result = result.replace(pattern, s2);
                        }

                        return result;
                    }))
                    .orElse(rawPath);
        }
    }
}
