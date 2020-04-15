package ru.tinkoff.qa.neptune.http.api.request.body;

import java.io.InputStream;
import java.net.http.HttpRequest;
import java.util.function.Supplier;

import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
import static java.util.Optional.ofNullable;

class StreamSuppliedBody extends RequestBody<Supplier<InputStream>> {

    StreamSuppliedBody(Supplier<InputStream> body) {
        super(ofNullable(body).orElseThrow());
    }

    @Override
    protected HttpRequest.BodyPublisher createPublisher() {
        return ofInputStream(body());
    }
}
