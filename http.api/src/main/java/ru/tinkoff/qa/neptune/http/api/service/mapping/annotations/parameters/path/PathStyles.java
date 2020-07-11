package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path;

import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.join;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.objectToMap;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.toStream;

/**
 * Path parameters support the following style values:
 * <ul>
 *     <li>
 *         simple – (default) comma-separated values
 *     </li>
 *     <li>
 *         label – dot-prefixed values, also known as label expansion
 *     </li>
 *     <li>
 *         matrix – semicolon-prefixed values, also known as path-style expansion.
 *     </li>
 * </ul>
 */
public enum PathStyles {
    /**
     * (default) comma-separated values
     */
    SIMPLE {
        @Override
        String arrayValue(Stream<String> valueSource, String ignored, boolean ignoreExplode) {
            return valueSource.collect(joining(","));
        }

        @Override
        String mapValue(Map<?, ?> map, String ignored, boolean explode) {
            var toJoin = new LinkedList<String>();
            map.forEach((o, o2) -> {
                var val = getEncoded(o2);
                if (!explode) {
                    toJoin.add(o + "," + val);
                } else {
                    toJoin.add(o + "=" + val);
                }
            });

            return arrayValue(toJoin.stream(), ignored, explode);
        }
    },

    /**
     * dot-prefixed values, also known as label expansion
     */
    LABEL {
        @Override
        String arrayValue(Stream<String> valueSource, String ignored, boolean ignoreExplode) {
            return valueSource.map(s -> "." + s).collect(joining());
        }

        @Override
        String mapValue(Map<?, ?> map, String ignored, boolean explode) {
            var toJoin = new LinkedList<String>();
            map.forEach((o, o2) -> {
                var val = getEncoded(o2);
                if (!explode) {
                    toJoin.add(o + "." + val);
                } else {
                    toJoin.add(o + "=" + val);
                }
            });

            return arrayValue(toJoin.stream(), ignored, explode);
        }
    },

    /**
     * semicolon-prefixed values, also known as path-style expansion
     */
    MATRIX {
        @Override
        String arrayValue(Stream<String> valueSource, String varName, boolean explode) {
            if (!explode) {
                return ";" + varName + "=" + valueSource.collect(joining(","));
            } else {
                return valueSource.map(s -> ";" + varName + "=" + s).collect(joining());
            }
        }

        @Override
        String mapValue(Map<?, ?> map, String varName, boolean explode) {
            var toJoin = new LinkedList<String>();
            map.forEach((o, o2) -> {
                var val = getEncoded(o2);
                if (!explode) {
                    toJoin.add(o + "," + val);
                } else {
                    toJoin.add(";" + o + "=" + val);
                }
            });

            var result = join(!explode ? "," : "", toJoin);
            if (!explode) {
                result = ";" + varName + "=" + result;
            }
            return result;
        }
    };

    private static String getEncoded(Object o) {
        return ofNullable(toStream(o))
                .map(stream -> stream.map(o1 ->
                        encode(String.valueOf(o1), UTF_8))
                        .collect(joining(",")))
                .orElseGet(() -> encode(String.valueOf(o), UTF_8));
    }

    String getPathValue(Object pathVarValue, String varName, boolean explode) {
        var stream = toStream(pathVarValue);
        if (stream != null) {
            return arrayValue(stream.map(PathStyles::getEncoded),
                    varName,
                    explode);
        }

        return ofNullable(objectToMap(pathVarValue))
                .map(map -> mapValue(map, varName, explode))
                .orElseGet(() -> arrayValue(of(getEncoded(pathVarValue)).stream(), varName, explode));

    }

    abstract String arrayValue(Stream<String> valueSource,
                               String varName,
                               boolean explode);

    abstract String mapValue(Map<?, ?> map,
                             String varName,
                             boolean explode);
}
