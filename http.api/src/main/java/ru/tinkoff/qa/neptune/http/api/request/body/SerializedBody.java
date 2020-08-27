package ru.tinkoff.qa.neptune.http.api.request.body;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpRequest;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.Optional.ofNullable;

public final class SerializedBody extends RequestBody<String> {

    private final String toStringExpr;

    private SerializedBody(String serialized, String toStringExpr) {
        super(ofNullable(serialized).orElseThrow());
        this.toStringExpr = toStringExpr;
    }

    SerializedBody(ObjectMapper mapper, Object body) {
        this(serialize(mapper, body), serializePretty(mapper, body));
    }

    private static String serialize(ObjectMapper mapper, Object body) {
        try {
            return mapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String serializePretty(ObjectMapper mapper, Object body) {
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
        return toStringExpr;
    }
}
