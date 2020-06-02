package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Date;

import static java.lang.System.lineSeparator;
import static java.util.Optional.ofNullable;

public final class ResponseCaptor extends StringCaptor<HttpResponse<?>> {

    private static final String LINE_SEPARATOR = lineSeparator();

    public ResponseCaptor() {
        super("Response");
    }

    private static void logSSL(Object toLog, String name, StringBuilder builder) {
        ofNullable(toLog).ifPresent(o ->
                builder.append(name).append(": ")
                        .append(o)
                        .append(LINE_SEPARATOR));
    }

    @Override
    public StringBuilder getData(HttpResponse<?> caught) {
        var stringBuilder = new StringBuilder()
                .append("Status code: ")
                .append(caught.statusCode())
                .append(LINE_SEPARATOR)

                .append("Response URI: ")
                .append(caught.uri())
                .append(LINE_SEPARATOR)

                .append("Corresponding request: ")
                .append(caught.request())
                .append(LINE_SEPARATOR);

        var headerMap = caught.headers().map();
        if (headerMap.size() > 0) {
            stringBuilder.append("Response headers: ")
                    .append(headerMap)
                    .append(LINE_SEPARATOR);
        }

        var body = caught.body();
        if (body == null) {
            stringBuilder
                    .append("Body: ")
                    .append("<NULL BODY>")
                    .append(LINE_SEPARATOR);
        } else {
            var cls = body.getClass();
            if (Void.class.isAssignableFrom(cls)) {
                stringBuilder
                        .append("Body: ")
                        .append("<VOID BODY>")
                        .append(LINE_SEPARATOR);
            } else if (byte[].class.isAssignableFrom(cls)) {
                stringBuilder
                        .append("Body: ")
                        .append("Byte array of length ")
                        .append(((byte[]) body).length)
                        .append(LINE_SEPARATOR);
            } else if (InputStream.class.isAssignableFrom(cls)) {
                stringBuilder
                        .append("Body: ")
                        .append("Input stream");
                try {
                    var bytes = ((InputStream) body).readAllBytes();
                    stringBuilder.append(" of length ")
                            .append(bytes.length)
                            .append(LINE_SEPARATOR);
                } catch (IOException e) {
                    stringBuilder
                            .append(". Length of the byte array is not available currently")
                            .append(LINE_SEPARATOR);
                }
            }
        }

        caught.sslSession().ifPresent(sslSession -> {
            ofNullable(sslSession.getSessionContext()).ifPresent(c ->
                    stringBuilder.append("SSL Context: ")
                            .append(c)
                            .append(LINE_SEPARATOR));
            logSSL(Arrays.toString(sslSession.getId()), "SSL Session Id", stringBuilder);
            logSSL(sslSession.isValid(), "SSL is valid", stringBuilder);
            logSSL(sslSession.getSessionContext(), "SSL Context", stringBuilder);
            logSSL(sslSession.getProtocol(), "SSL Protocol", stringBuilder);
            logSSL(sslSession.getPeerHost(), "SSL Peer host", stringBuilder);
            logSSL(sslSession.getPeerPort(), "SSL Peer port", stringBuilder);

            logSSL(new Date(sslSession.getCreationTime()), "SSL Creation time", stringBuilder);

            var lastAccessed = sslSession.getLastAccessedTime();
            if (lastAccessed > 0L) {
                logSSL(new Date(lastAccessed), "SSL last accessed", stringBuilder);
            }

            var values = sslSession.getValueNames();
            if (values.length > 0) {
                logSSL(Arrays.toString(values), "SSL values", stringBuilder);
            }

            var certs = sslSession.getLocalCertificates();
            if (certs.length > 0) {
                logSSL(Arrays.toString(certs), "SSL local certificates", stringBuilder);
            }

            try {
                var certs2 = sslSession.getPeerCertificates();
                if (certs2.length > 0) {
                    logSSL(Arrays.toString(certs2), "SSL peer certificates", stringBuilder);
                }
            } catch (SSLPeerUnverifiedException e) {
                logSSL(e.getMessage(), "SSL peer certificates", stringBuilder);
            }

            logSSL(sslSession.getPacketBufferSize(), "SSL Packet buffer size", stringBuilder);
            logSSL(sslSession.getApplicationBufferSize(), "SSL Application buffer size", stringBuilder);
            logSSL(sslSession.getCipherSuite(), "SSL Cipher suite", stringBuilder);
            logSSL(sslSession.getLocalPrincipal(), "Local principal", stringBuilder);
        });
        return stringBuilder;
    }

    @Override
    public HttpResponse<?> getCaptured(Object toBeCaptured) {
        return ofNullable(toBeCaptured)
                .map(o -> {
                    var cls = o.getClass();
                    if (HttpResponse.class.isAssignableFrom(cls)) {
                        return (HttpResponse<?>) o;
                    } else {
                        return null;
                    }
                })
                .orElse(null);
    }
}
