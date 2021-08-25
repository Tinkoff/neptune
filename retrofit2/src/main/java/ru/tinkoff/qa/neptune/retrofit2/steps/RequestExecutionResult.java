package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;

final class RequestExecutionResult<M, T> {

    private final Response lastResponse;
    private final M callBody;
    private final T result;

    RequestExecutionResult(Response lastResponse, M callBody, T result) {
        this.lastResponse = lastResponse;
        this.callBody = callBody;
        this.result = result;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    T getResult() {
        return result;
    }

    M getCallBody() {
        return callBody;
    }

    @Override
    public String toString() {
        return lastResponse.toString();
    }
}
