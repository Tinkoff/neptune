package ru.tinkoff.qa.neptune.http.api.request.body;

import java.net.http.HttpRequest;

import static java.net.http.HttpRequest.BodyPublishers.noBody;

public final class EmptyBody extends RequestBody<Void> {

    EmptyBody() {
        super(null);
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return noBody();
    }

    @Override
    public String toString() {
        return "<empty>";
    }
}
