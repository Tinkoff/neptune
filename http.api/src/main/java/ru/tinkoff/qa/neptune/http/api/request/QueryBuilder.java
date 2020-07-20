package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

class QueryBuilder {

    private final static List<String> RESERVED = List.of(";",
            "/",
            "?",
            ":",
            "@",
            "&",
            "=",
            "+",
            "$",
            ",",
            "[",
            "]",
            "'");

    private final Set<NameAndValue> queries = new LinkedHashSet<>();

    void addParameter(String name,
                      boolean toExpand,
                      QueryValueDelimiters delimiter,
                      boolean allowReserved,
                      Object... values) {
        checkArgument(isNotBlank(name), "Name of the parameter should not be null/blank");
        checkNotNull(values);
        checkArgument(values.length > 0,
                format("It is necessary to define at least one value of the parameter '%s'", name));

        var nameValue = (NameAndValue) queries.stream()
                .filter(o -> Objects.equals(o.getName(), name))
                .findFirst()
                .orElse(null);

        if (nameValue == null) {
            nameValue = new NameAndValue(name, toExpand, delimiter, allowReserved);
            queries.add(nameValue);
        }

        nameValue.addValues(values);
    }

    URI appendURI(URI uri) {
        checkNotNull(uri);
        var query = queries.stream()
                .map(NameAndValue::toString)
                .collect(joining("&"));

        if (isBlank(query)) {
            return uri;
        } else {
            var path = uri.getPath();
            path = isBlank(path) ? "/" : path;
            var resultQuery = uri.getQuery();
            resultQuery = isBlank(resultQuery) ? query : resultQuery + "&" + query;
            try {
                return new URI(uri.getScheme(),
                        uri.getUserInfo(),
                        uri.getHost(),
                        uri.getPort(),
                        path, resultQuery,
                        uri.getFragment());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static class NameAndValue {

        private final String name;
        private final List<Object> values = new LinkedList<>();
        private final boolean toExpand;
        private final QueryValueDelimiters delimiter;
        private final boolean allowReserved;

        private NameAndValue(String name, boolean toExpand, QueryValueDelimiters delimiter, boolean allowReserved) {
            this.name = name;
            this.toExpand = toExpand;
            this.delimiter = delimiter;
            this.allowReserved = allowReserved;
        }

        private static Stream<?> prepareStreamOfObjects(Object value) {
            var cls = value.getClass();

            if (cls.isArray()) {
                Object[] result;
                if (cls.getComponentType().isPrimitive()) {
                    if (byte[].class.equals(cls)) {
                        result = toObject((byte[]) value);
                    } else if (short[].class.equals(cls)) {
                        result = toObject((short[]) value);
                    } else if (int[].class.equals(cls)) {
                        result = toObject((int[]) value);
                    } else if (long[].class.equals(cls)) {
                        result = toObject((long[]) value);
                    } else if (float[].class.equals(cls)) {
                        result = toObject((float[]) value);
                    } else if (double[].class.equals(cls)) {
                        result = toObject((double[]) value);
                    } else if (boolean[].class.equals(cls)) {
                        result = toObject((boolean[]) value);
                    } else {
                        result = toObject((char[]) value);
                    }

                    return stream(result);
                } else {
                    return stream((Object[]) value);
                }
            } else if (Iterable.class.isAssignableFrom(cls)) {
                return StreamSupport.stream(((Iterable<?>) value).spliterator(), false);
            }

            return null;
        }

        private static String encode(String toBeEncoded, boolean allowReserved) {
            if (!allowReserved) {
                return URLEncoder.encode(toBeEncoded, UTF_8);
            }

            var builder = new StringBuilder();
            stream(toBeEncoded.split(""))
                    .forEach(s -> {
                        if (!RESERVED.contains(s)) {
                            builder.append(URLEncoder.encode(s, UTF_8));
                        } else {
                            builder.append(s);
                        }
                    });
            return builder.toString();
        }

        private static Stream<String> toStream(Object value, boolean allowReserved) {
            return ofNullable(prepareStreamOfObjects(value))
                    .map(stream -> stream.map(o -> {
                        var streamToTransform = prepareStreamOfObjects(o);
                        if (streamToTransform == null) {
                            return encode(valueOf(o), allowReserved);
                        }

                        return streamToTransform
                                .map(o1 -> encode(valueOf(o1), allowReserved))
                                .collect(joining(","));
                    }))
                    .orElseGet(() -> toStream(new Object[]{value}, allowReserved));
        }

        void addValues(Object... values) {
            checkNotNull(values);
            checkArgument(values.length > 0,
                    format("It is necessary to define at least one value of the parameter '%s'", name));
            this.values.addAll(stream(values)
                    .filter(Objects::nonNull)
                    .collect(toList()));
        }

        String getName() {
            return name;
        }

        public String toString() {
            var stingEncodedValues = toStream(values, allowReserved);
            if (toExpand) {
                return stingEncodedValues
                        .map(s -> name + "=" + s)
                        .collect(joining("&"));
            } else {
                return stingEncodedValues
                        .collect(joining(delimiter.toString(), name + "=", ""));
            }
        }
    }
}
