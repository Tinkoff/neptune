package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path;

import java.nio.ByteBuffer;
import java.util.BitSet;

import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.toStream;

final class PathEncodingUtil {

    private static final BitSet UNRESERVED = new BitSet(256);
    private static final BitSet PATHSAFE = new BitSet(256);
    private static final int RADIX = 16;

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
        UNRESERVED.set('!');
        UNRESERVED.set('~');
        UNRESERVED.set('\'');
        UNRESERVED.set('(');
        UNRESERVED.set(')');


        // URL path safe
        PATHSAFE.or(UNRESERVED);
        PATHSAFE.set('/'); // segment separator
        PATHSAFE.set(';'); // param separator
        PATHSAFE.set(':'); // rest as per list in 2396, i.e. : @ & = + $ ,
        PATHSAFE.set('@');
        PATHSAFE.set('&');
        PATHSAFE.set('=');
        PATHSAFE.set('+');
        PATHSAFE.set('$');
        PATHSAFE.set(',');
    }

    private PathEncodingUtil() {
        super();
    }

    private static String encodePathSegment(Object content) {
        if (content == null) {
            return null;
        }

        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = UTF_8.encode(valueOf(content));
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (PATHSAFE.get(b)) {
                buf.append((char) b);
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

    static String getEncoded(Object o) {
        return ofNullable(toStream(o))
                .map(stream -> stream
                        .map(PathEncodingUtil::encodePathSegment)
                        .collect(joining(",")))
                .orElseGet(() -> encodePathSegment(o));
    }
}
