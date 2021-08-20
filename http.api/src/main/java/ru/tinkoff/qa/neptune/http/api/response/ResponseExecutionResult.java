package ru.tinkoff.qa.neptune.http.api.response;

import java.net.http.HttpResponse;
import java.util.function.Function;

final class ResponseExecutionResult<T, R> {

    private final HttpResponse<T> response;
    private final R result;

    ResponseExecutionResult(HttpResponse<T> response, Function<T, R> f) {
        this.response = response;
        result = f.apply(response.body());
    }

    HttpResponse<T> getResponse() {
        return response;
    }

    public R getResult() {
        return result;
    }
}
