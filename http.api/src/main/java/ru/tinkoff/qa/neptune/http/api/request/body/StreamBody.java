package ru.tinkoff.qa.neptune.http.api.request.body;

import java.io.InputStream;
import java.net.http.HttpRequest;

import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
import static java.util.Optional.ofNullable;

class StreamBody extends RequestBody<InputStream> {

    StreamBody(InputStream body) {
        super(ofNullable(body).orElseThrow());
    }

    @Override
    protected HttpRequest.BodyPublisher createPublisher() {
        return ofInputStream(() -> body());
    }
}
