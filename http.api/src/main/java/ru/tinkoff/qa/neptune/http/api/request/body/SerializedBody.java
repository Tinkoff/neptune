package ru.tinkoff.qa.neptune.http.api.request.body;

import ru.tinkoff.qa.neptune.http.api.dto.DTObject;

import java.net.http.HttpRequest;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.Optional.ofNullable;

class SerializedBody extends RequestBody<String> {

    SerializedBody(DTObject body) {
        super(ofNullable(body)
                .map(DTObject::serialize)
                .orElseThrow());
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofString(super.body());
    }
}
