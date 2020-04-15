package ru.tinkoff.qa.neptune.http.api.request.body;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.util.Map;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class URLEncodedForm extends RequestBody<String> {

    private final Charset charset;

    URLEncodedForm(Map<String, String> formParameters, Charset charset) {
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
        this.charset = charset;
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofString(super.body(), charset);
    }

    @Override
    public String body() {
        return new String(super.body().getBytes(charset));
    }
}
