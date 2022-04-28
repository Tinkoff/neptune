package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Http response")
class Response<T, R> implements HttpResponse<T> {

    private final HttpResponse<T> response;
    private final R calculated;

    Response(HttpResponse<T> response, R calculated) {
        this.response = response;
        this.calculated = calculated;
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }

    @Override
    public HttpRequest request() {
        return response.request();
    }

    @Override
    public Optional<HttpResponse<T>> previousResponse() {
        return response.previousResponse();
    }

    @Override
    public HttpHeaders headers() {
        return response.headers();
    }

    @Override
    public T body() {
        return response.body();
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return response.sslSession();
    }

    @Override
    public URI uri() {
        return response.uri();
    }

    @Override
    public HttpClient.Version version() {
        return response.version();
    }

    public String toString() {
        return translate(this) + " " + response.toString();
    }

    public R getCalculated() {
        return calculated;
    }
}
