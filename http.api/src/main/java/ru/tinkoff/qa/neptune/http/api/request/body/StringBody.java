package ru.tinkoff.qa.neptune.http.api.request.body;

import java.net.http.HttpRequest;
import java.nio.charset.Charset;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.Optional.ofNullable;

public final class StringBody extends RequestBody<String> {

    private final Charset charset;

    StringBody(String body, Charset charset) {
        super(ofNullable(body).orElseThrow());
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

    @Override
    public String toString() {
        return body();
    }
}
