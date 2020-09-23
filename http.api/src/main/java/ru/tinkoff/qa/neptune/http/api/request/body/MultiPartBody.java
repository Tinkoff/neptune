package ru.tinkoff.qa.neptune.http.api.request.body;

import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;

import java.net.http.HttpRequest;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArrays;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class MultiPartBody extends RequestBody<BodyPart[]> {

    private final String boundary;

    MultiPartBody(String boundary, BodyPart... body) {
        super(ofNullable(body)
                .map(bodyParts -> {
                    checkArgument(body.length > 0, "No one body part was defined");
                    return bodyParts;
                }).orElseThrow());
        checkArgument(isNotBlank(boundary), "Boundary is not defined");
        this.boundary = boundary;
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        var byteArrays = new ArrayList<byte[]>();
        stream(body()).forEach(bodyPart -> {
            byteArrays.addAll(bodyPart.content("--" + boundary));
        });
        byteArrays.add(("--" + boundary + "--").getBytes(UTF_8));
        return ofByteArrays(byteArrays);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        stream(body()).forEach(bodyPart -> builder.append("--")
                .append(boundary).append("\r\n")
                .append(bodyPart.toString()));
        return builder.append("--")
                .append(boundary)
                .append("--")
                .toString();
    }

    public String getBoundary() {
        return boundary;
    }
}
