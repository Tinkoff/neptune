package ru.tinkoff.qa.neptune.http.api.request.body;

import java.net.http.HttpRequest;

import static java.lang.Integer.valueOf;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.checkFromIndexSize;

final class ByteArrayBody extends RequestBody<byte[]> {

    private final Integer length;
    private final Integer offset;

    private ByteArrayBody(byte[] body, Integer length, Integer offset) {
        super(body);
        checkFromIndexSize(offset, length, body.length);
        this.length = length;
        this.offset = offset;
    }

    ByteArrayBody(byte[] body) {
        this(body, null, null);
    }

    ByteArrayBody(byte[] body, int length, int offset) {
        this(body, valueOf(length), valueOf(offset));
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        if (length == null || offset == null) {
            return ofByteArray(super.body());
        }

        return ofByteArray(super.body(), offset, length);
    }

    public byte[] body() {
        if (length == null || offset == null) {
            return super.body();
        }

        return copyOfRange(super.body(), offset, length);
    }

    @Override
    public String toString() {
        return "Byte array. Length: " + body().length;
    }
}
