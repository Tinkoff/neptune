package ru.tinkoff.qa.neptune.http.api.request;

import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Optional;

/**
 * Wraps java {@link HttpRequest} and its body
 */
public final class NeptuneHttpRequestImpl extends HttpRequest {

    private final HttpRequest wrapped;
    private final RequestBody<?> body;

    NeptuneHttpRequestImpl(HttpRequest wrapped, RequestBody<?> body) {
        this.wrapped = wrapped;
        this.body = body;
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        return wrapped.bodyPublisher();
    }

    @Override
    public String method() {
        return wrapped.method();
    }

    @Override
    public Optional<Duration> timeout() {
        return wrapped.timeout();
    }

    @Override
    public boolean expectContinue() {
        return wrapped.expectContinue();
    }

    @Override
    public URI uri() {
        return wrapped.uri();
    }

    @Override
    public Optional<HttpClient.Version> version() {
        return wrapped.version();
    }

    @Override
    public HttpHeaders headers() {
        return wrapped.headers();
    }

    /**
     * Returns body of the request.
     *
     * @return request body
     */
    public RequestBody<?> body() {
        return body;
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }
}
