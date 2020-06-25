package ru.tinkoff.qa.neptune.http.api.request.body;

import java.io.InputStream;
import java.net.http.HttpRequest;
import java.util.function.Supplier;

import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public final class StreamSuppliedBody extends RequestBody<Supplier<InputStream>> {

    StreamSuppliedBody(Supplier<InputStream> body) {
        super(ofNullable(body).orElseThrow());
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofInputStream(body());
    }

    @Override
    public String toString() {
        var body = body();
        String supplierName;
        if (isLoggable(body)) {
            supplierName = body.toString();
        } else {
            Class<?> cls = body().getClass();
            if (cls.isAnonymousClass()) {
                cls = cls.getSuperclass();
            }
            supplierName = cls.getName();
        }
        return "Input stream supplied by '" + supplierName + "'";
    }
}
