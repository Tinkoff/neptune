package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.toStream;

class QueryBuilder {

    private static final int RADIX = 16;
    private static final BitSet URLENCODER = new BitSet(256);
    private static final BitSet URIC = new BitSet(256);
    private static final BitSet RESERVED = new BitSet(256);
    private static final BitSet UNRESERVED = new BitSet(256);

    static {
        // unreserved chars
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            UNRESERVED.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            UNRESERVED.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            UNRESERVED.set(i);
        }
        UNRESERVED.set('_'); // these are the charactes of the "mark" list
        UNRESERVED.set('-');
        UNRESERVED.set('.');
        UNRESERVED.set('*');
        URLENCODER.or(UNRESERVED); // skip remaining unreserved characters
        UNRESERVED.set('!');
        UNRESERVED.set('~');
        UNRESERVED.set('\'');
        UNRESERVED.set('(');
        UNRESERVED.set(')');

        RESERVED.set(';');
        RESERVED.set('/');
        RESERVED.set('?');
        RESERVED.set(':');
        RESERVED.set('@');
        RESERVED.set('&');
        RESERVED.set('=');
        RESERVED.set('+');
        RESERVED.set('$');
        RESERVED.set(',');
        RESERVED.set('['); // added by RFC 2732
        RESERVED.set(']'); // added by RFC 2732

        URIC.or(RESERVED);
        URIC.or(UNRESERVED);
    }

    private final Set<Object> queries = new LinkedHashSet<>();

    private static String urlEncode(final String content,
                                    final BitSet safechars,
                                    final boolean blankAsPlus) {
        if (content == null) {
            return null;
        }

        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = UTF_8.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (safechars.get(b)) {
                buf.append((char) b);
            } else if (blankAsPlus && b == ' ') {
                buf.append('+');
            } else {
                buf.append("%");
                final char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, RADIX));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, RADIX));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }

    void addParameter(String name, final Object... values) {
        checkNotNull(name);
        checkNotNull(values);
        checkArgument(values.length > 0,
                format("It is necessary to define at least one value of the parameter '%s'", name));

        var nameValue = (NameAndValue) queries.stream()
                .filter(o -> (o instanceof NameAndValue) && Objects.equals(((NameAndValue) o).getName(), name))
                .findFirst()
                .orElse(null);

        if (nameValue == null) {
            nameValue = new NameAndValue(name);
            queries.add(nameValue);
        }

        nameValue.addValues(values);
    }

    void addQueryPart(String queryFragment) {
        checkArgument(isNotBlank(queryFragment), "A query part to add should not be null or blank");
        queries.add(queryFragment);
    }

    URI appendURI(URI uri) {
        checkNotNull(uri);
        var query = queries.stream()
                .map(o -> {
                    if (o instanceof String) {
                        return urlEncode(valueOf(o), URIC, false);
                    }

                    if (o instanceof NameAndValue) {
                        var nameAndValue = (NameAndValue) o;
                        var encodedName = urlEncode(nameAndValue.getName(), URLENCODER, true);
                        return nameAndValue.getValues()
                                .stream()
                                .map(o1 -> {
                                    var stream = toStream(o1);
                                    return ofNullable(stream)
                                            .map(stream1 -> encodedName + "="
                                                    + urlEncode(stream1
                                                    .map(String::valueOf)
                                                    .collect(joining(",")), URIC, false))
                                            .orElseGet(() -> encodedName + "=" + urlEncode(valueOf(o1), URLENCODER, true));
                                })
                                .collect(joining("&"));
                    }

                    return null;
                })
                .filter(Objects::nonNull)
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

        private NameAndValue(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void addValues(Object... values) {
            checkNotNull(values);
            checkArgument(values.length > 0,
                    format("It is necessary to define at least one value of the parameter '%s'", name));
            this.values.addAll(stream(values)
                    .filter(Objects::nonNull)
                    .collect(toList()));
        }

        List<Object> getValues() {
            return values;
        }
    }
}
