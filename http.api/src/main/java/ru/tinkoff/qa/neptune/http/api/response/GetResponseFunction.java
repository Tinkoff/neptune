package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

final class GetResponseFunction<T> implements Function<HttpStepContext, HttpResponse<T>> {

    private final ResponseExecutionInfo info;
    private final HttpRequest request;
    private final HttpResponse.BodyHandler<T> bodyHandler;

    GetResponseFunction(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        this.bodyHandler = bodyHandler;
        info = new ResponseExecutionInfo();
        this.request = request;
    }

    @Override
    public HttpResponse<T> apply(HttpStepContext httpStepContext) {
        try {
            info.setLastReceived(null);
            info.startExecutionLogging();
            var received = httpStepContext.getCurrentClient().send(request, bodyHandler);
            info.setLastReceived(received);
            return new Response<>(received);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            info.stopExecutionLogging();
        }
    }

    NeptuneHttpRequestImpl getRequest() {
        return (NeptuneHttpRequestImpl) request;
    }

    ResponseExecutionInfo getInfo() {
        return info;
    }
}
