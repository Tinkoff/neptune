package ru.tinkoff.qa.neptune.http.api.request.body;

import java.net.http.HttpRequest;

/**
 * Wraps a body of http request and creates an instance of {@link java.net.http.HttpRequest.BodyPublisher}
 *
 * @param <T> is a type of a request body
 */
public abstract class RequestBody<T> {

    private final T body;

    protected RequestBody(T body) {
        this.body = body;
    }

    /**
     * Creates an instance of {@link java.net.http.HttpRequest.BodyPublisher}
     *
     * @return an instance of {@link java.net.http.HttpRequest.BodyPublisher}
     */
    public abstract HttpRequest.BodyPublisher createPublisher();

    /**
     * Returns an object that represents a body of http response
     *
     * @return a value of http response body
     */
    public T body() {
        return body;
    }

    @Override
    public abstract String toString();
}
