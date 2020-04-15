package ru.tinkoff.qa.neptune.http.api.request.body;

import java.net.http.HttpRequest;

import static ru.tinkoff.qa.neptune.http.api.request.CommonBodyPublishers.empty;

class EmptyBody extends RequestBody<Void> {

    EmptyBody() {
        super(null);
    }

    @Override
    protected HttpRequest.BodyPublisher createPublisher() {
        return empty();
    }
}
