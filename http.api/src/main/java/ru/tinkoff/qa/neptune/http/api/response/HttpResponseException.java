package ru.tinkoff.qa.neptune.http.api.response;

public final class HttpResponseException extends RuntimeException {
    HttpResponseException(Throwable t) {
        super(t);
    }
}
