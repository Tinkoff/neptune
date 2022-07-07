package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path;

import org.apache.commons.lang3.StringUtils;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static java.lang.String.format;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.*;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.getFromMethod;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.SIMPLE;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those value
 * forms a path of a request URI.
 * <p>
 * Required types:
 *     <ul>
 *         <li>primitive</li>
 *         <li>primitive wrappers</li>
 *         <li>{@link String}</li>
 *         <li>Any object whose string representation may be read as a part of an URI path value correctly. See {@link Object#toString()}</li>
 *         <li>Arrays of types described above</li>
 *         <li>Iterable of described above</li>
 *         <li>Maps of keys and values of types described above</li>
 *         <li>A POJO whose class has fields of following types: primitive types, primitive wrappers,
 *         {@link String}, any type of an object whose string representation may be read as a part of an URI path
 *         value correctly, see {@link Object#toString()}, arrays and iterables of listed types. Class of a POJO should
 *         extend {@link ru.tinkoff.qa.neptune.http.api.mapping.MappedObject}</li>
 *     </ul>
 * </p>
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface PathParameter {

    /**
     * @return name of a path variable
     */
    String name();

    /**
     * @return is parameter required or not. Default value is {@code true}.
     * It allows or doesn't allow {@code null} values of a parameter on a method
     * invocation.
     */
    boolean required() default true;

    /**
     * @return to explode parameter value or not. Default value is {@code false}.
     * This has an effect when parameter value has type {@link Map} or type of some POJO.
     */
    boolean explode() default false;

    /**
     * @return style of a path variable. Default is {@link PathStyles#SIMPLE}
     */
    PathStyles style() default SIMPLE;

    /**
     * Util class that reads parameters of a {@link java.lang.reflect.Method} and
     * forms map of variables and their values to insert it to {@link URI} as path.
     */
    final class PathParameterReader {

        private static String validatePathMappingAndGetPattern(Method toRead) {
            var pattern = ofNullable(toRead.getAnnotation(URIPath.class))
                    .map(uriPath -> {
                        var path = uriPath.value();
                        if (isBlank(path)) {
                            throw new UnsupportedOperationException(
                                    format("A blank value of uri-path is defined for the %s", toRead));
                        }
                        return path;
                    })
                    .orElse(null);

            var patternParams = ofNullable(pattern)
                    .map(s -> asList(ofNullable(substringsBetween(s, "{", "}"))
                            .orElseGet(() -> new String[]{})))
                    .orElse(of());

            var pathParams = stream(toRead.getParameterAnnotations())
                    .map(annotations -> stream(annotations)
                            .filter(a -> PathParameter.class.equals(a.annotationType()))
                            .findFirst()
                            .map(a -> ((PathParameter) a).name())
                            .orElse(null))
                    .filter(StringUtils::isNoneBlank)
                    .collect(toList());

            var notDefinedByParameters = patternParams
                    .stream()
                    .filter(s -> !pathParams.contains(s))
                    .collect(toList());

            if (!notDefinedByParameters.isEmpty()) {
                throw new IllegalArgumentException(format("These parameters are not defined by method parameters: %s. " +
                                "These names are defined by pattern %s",
                        notDefinedByParameters,
                        pattern));
            }

            var notDefinedByPattern = pathParams
                    .stream()
                    .filter(s -> !patternParams.contains(s))
                    .collect(toList());

            if (!notDefinedByPattern.isEmpty()) {
                throw new IllegalArgumentException(format("These parameters are not defined by pattern: %s. " +
                                "Pattern: %s",
                        notDefinedByPattern,
                        ofNullable(pattern).orElse("<empty>")));
            }

            var unique = new ArrayList<>();
            for (var p : pathParams) {
                if (!unique.contains(p)) {
                    unique.add(p);
                } else {
                    throw new UnsupportedOperationException(format("Path variable '%s' is defined more than once. " +
                            "This is not supported", p));
                }
            }

            return pattern;
        }

        /**
         * Reads parameters of a {@link java.lang.reflect.Method} and parameters of its current invocation
         * and then creates URI-path to requested endpoint.
         *
         * @param toRead     is a method to be read
         * @param parameters parameters of current invocation of the method
         * @return string URI-path to requested endpoint.
         */
        public static String readPathParameters(Method toRead, Object[] parameters) {
            var pattern = validatePathMappingAndGetPattern(toRead);

            if (pattern == null) {
                return null;
            }

            return ofNullable(getFromMethod(toRead,
                    PathParameter.class,
                    parameters,
                    (ps, params) -> {
                        var map = new HashMap<String, String>();
                        for (int i = 0; i < ps.length; i++) {
                            var annotation = ps[i].getAnnotation(PathParameter.class);
                            map.put(annotation.name(),
                                    ofNullable(params[i])
                                            .map(o -> annotation.style().getPathValue(o,
                                                    annotation.name(),
                                                    annotation.explode()
                                            ))
                                            .orElseGet(() -> {
                                                if (annotation.required()) {
                                                    throw new IllegalArgumentException(format("Path variable '%s' requires value " +
                                                                    "that differs from null",
                                                            annotation.name()));
                                                }

                                                return EMPTY;
                                            }));
                        }

                        var result = pattern;
                        for (var e : map.entrySet()) {
                            var s = e.getKey();
                            var s2 = e.getValue();
                            result = result.replace("{" + s + "}", s2);
                        }


                        result = result.replace("//", "/");

                        if (result.startsWith("/")) {
                            return result.substring(1);
                        }

                        return result;
                    }))
                    .orElse(pattern);
        }
    }
}
