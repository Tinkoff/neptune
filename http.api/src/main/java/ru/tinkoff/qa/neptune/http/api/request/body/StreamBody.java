package ru.tinkoff.qa.neptune.http.api.request.body;

import java.io.InputStream;
import java.net.http.HttpRequest;

import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
import static java.util.Optional.ofNullable;

final class StreamBody extends RequestBody<InputStream> {

    StreamBody(InputStream body) {
        super(ofNullable(body).orElseThrow());
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofInputStream(this::body);
    }

    @Override
    public String toString() {
        var stream = body();

        try {
            return "Input stream body. Available length of a byte array at the moment is " + stream.available();
        } catch (Exception e) {
            return "Input stream body. Length of the byte array is not available currently";
        }
    }
}
