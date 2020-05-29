package ru.tinkoff.qa.neptune.http.api.request.body;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.http.api.dto.DTObject;

import java.net.http.HttpRequest;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.Optional.ofNullable;

public final class SerializedBody extends RequestBody<String> {

    private SerializedBody(String serialized) {
        super(ofNullable(serialized)
                .orElseThrow());
    }

    SerializedBody(DTObject body) {
        this(ofNullable(body)
                .map(DTObject::serialize)
                .orElseThrow());
    }

    SerializedBody(ObjectMapper mapper, Object body) {
        this(serialize(mapper, body));
    }

    private static String serialize(ObjectMapper mapper, Object body) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
