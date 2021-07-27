package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;

final class RequestExecutionResult<T> {

    private final Response lastResponse;
    private final T result;

    RequestExecutionResult(Response lastResponse, T result) {
        this.lastResponse = lastResponse;
        this.result = result;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public T getResult() {
        return result;
    }
}
