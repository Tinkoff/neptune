package ru.tinkoff.qa.neptune.http.api.response;

import java.net.http.HttpResponse;

final class ResponseExecutionResult<T, R> {

    private final HttpResponse<T> response;
    private final R result;

    ResponseExecutionResult(HttpResponse<T> response, R result) {
        this.response = response;
        this.result = result;
    }

    HttpResponse<T> getResponse() {
        return response;
    }

    public R getResult() {
        return result;
    }

    public String toString() {
        return response.toString();
    }
}
