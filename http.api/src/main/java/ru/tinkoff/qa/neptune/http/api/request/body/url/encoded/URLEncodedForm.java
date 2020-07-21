package ru.tinkoff.qa.neptune.http.api.request.body.url.encoded;

import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.util.Map;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

public final class URLEncodedForm extends RequestBody<String> {

    public URLEncodedForm(Map<String, String> formParameters) {
        super(ofNullable(formParameters)
                .map(m -> m.entrySet()
                        .stream()
                        .map(entry -> {
                            try {
                                return format("%s=%s",
                                        encode(entry.getKey(), UTF_8.toString()),
                                        encode(entry.getValue(), UTF_8.toString()));
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(joining("&")))
                .orElseThrow());
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofString(super.body());
    }

    @Override
    public String toString() {
        return body();
    }
}
