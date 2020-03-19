package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

final class ForResponseFunction<T> implements Function<HttpStepContext, HttpResponse<T>> {

    private final RequestBuilder requestBuilder;
    private final HttpResponse.BodyHandler<T> bodyHandler;
    private HttpResponse<T> lastReceived;

    ForResponseFunction(RequestBuilder requestBuilder, HttpResponse.BodyHandler<T> bodyHandler) {
        checkArgument(nonNull(requestBuilder), "Http request should not be null");
        checkArgument(nonNull(bodyHandler), "Http response body handler should not be null");
        this.requestBuilder = requestBuilder;
        this.bodyHandler = bodyHandler;
    }

    @Override
    public HttpResponse<T> apply(HttpStepContext httpStepContext) {
        lastReceived = null;
        try {
            lastReceived =  httpStepContext.getCurrentClient().send(requestBuilder.build(), bodyHandler);
            return lastReceived;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    HttpResponse<T> getLastReceived() {
        return lastReceived;
    }
}
