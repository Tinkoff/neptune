package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
final class GetResponseFunction<T, R> implements Function<HttpStepContext, ResponseExecutionResult<T, R>> {

    private final ResponseExecutionInfo info;
    private final HttpRequest request;
    private final HttpResponse.BodyHandler<T> bodyHandler;
    private final Function<T, R> function;
    private final Predicate<? super R> predicate;

    GetResponseFunction(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler, Function<T, R> function, Predicate<? super R> predicate) {
        this.bodyHandler = bodyHandler;
        this.function = function;
        this.predicate = predicate;
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

        var result = function.apply(((HttpResponse<T>) info.getLastReceived()).body());
        if (ofNullable(predicate)
                .map(p -> p.test(result))
                .orElse(true)) {
            return new ResponseExecutionResult<>((HttpResponse<T>) info.getLastReceived(), result);
        }
        else {
            return null;
        }
    }

    NeptuneHttpRequestImpl getRequest() {
        return (NeptuneHttpRequestImpl) request;
    }

    ResponseExecutionInfo getInfo() {
        return info;
    }
}
