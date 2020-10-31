package ru.tinkoff.qa.neptune.core.api.utils;

import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.List;

import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;

/**
 * Util class that helps to encode parts of UPL/URI correctly.
 */
public final class URLEncodeUtil {

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

    private URLEncodeUtil() {
        super();
    }

    /**
     * Encodes a string that is supposed to be a substring of a query of an URI/URL. It is strongly recommended
     * to use simple strings instead of pieces of values concatenated by reserved characters
     *
     * @param toBeEncoded   is a string which is supposed to be a substring of a query of an URI/URL
     * @param allowReserved allow to keep reserved character as they are or not
     * @return encoded string
     */
    public static String encodeQuerySubstring(String toBeEncoded, boolean allowReserved) {

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

    /**
     * Encodes a string that is supposed to be a substring of a path of an URI/URL. It is strongly recommended
     * to use simple strings instead of pieces of values concatenated by {@code '/'}
     *
     * @param content is a string which is supposed to be a substring of a path of an URI/URL
     * @return encoded string
     */
    public static String encodePathSubstring(Object content) {
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
}
