package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

@SuppressWarnings("unchecked")
final class GetResponseFunction<T, R> implements Function<HttpStepContext, ResponseExecutionResult<T, R>> {

    private final ResponseExecutionInfo info;
    private final HttpRequest request;
    private final HttpResponse.BodyHandler<T> bodyHandler;
    private final Function<T, R> function;

    GetResponseFunction(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler, Function<T, R> function) {
        this.bodyHandler = bodyHandler;
        this.function = function;
        info = new ResponseExecutionInfo();
        this.request = request;
    }

    @Override
    public ResponseExecutionResult<T, R> apply(HttpStepContext httpStepContext) {
        try {
            info.setLastReceived(null);
            info.startExecutionLogging();
            info.setLastReceived(httpStepContext.getCurrentClient().send(request, bodyHandler));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            info.stopExecutionLogging();
        }

        return new ResponseExecutionResult<>((HttpResponse<T>) info.getLastReceived(), function);
    }

    NeptuneHttpRequestImpl getRequest() {
        return (NeptuneHttpRequestImpl) request;
    }

    ResponseExecutionInfo getInfo() {
        return info;
    }
}
