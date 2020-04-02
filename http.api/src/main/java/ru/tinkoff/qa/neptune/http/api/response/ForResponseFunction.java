package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Set.of;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

final class ForResponseFunction<T> implements Function<HttpStepContext, HttpResponse<T>> {

    private final RequestBuilder requestBuilder;
    private final HttpResponse.BodyHandler<T> bodyHandler;
    private HttpResponse<T> lastReceived;
    private static final Logger LOGGER = Logger.getLogger("jdk.httpclient.HttpClient");
    private RequestResponseLogCollector collector;
    private boolean toReportLog = true;

    ForResponseFunction(RequestBuilder requestBuilder, HttpResponse.BodyHandler<T> bodyHandler) {
        checkArgument(nonNull(requestBuilder), "Http request should not be null");
        checkArgument(nonNull(bodyHandler), "Http response body handler should not be null");
        this.requestBuilder = requestBuilder;
        this.bodyHandler = bodyHandler;
    }

    @Override
    public HttpResponse<T> apply(HttpStepContext httpStepContext) {
        lastReceived = null;
        collector = new RequestResponseLogCollector();
        var success = false;
        try {
            LOGGER.addHandler(collector);
            lastReceived = httpStepContext.getCurrentClient().send(requestBuilder.build(), bodyHandler);
            success = true;
            return lastReceived;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            LOGGER.removeHandler(collector);
            if (toReportLog && success && catchSuccessEvent()) {
                catchValue(collector, of(new CaptorFilterByProducedType(Object.class)));
            } else if (toReportLog && !success && catchFailureEvent()) {
                catchValue(collector, of(new CaptorFilterByProducedType(Object.class)));
            }
        }
    }

    HttpResponse<T> getLastReceived() {
        return lastReceived;
    }

    //TODO use it for step arguments
    HttpRequest getRequest() {
        return requestBuilder.build();
    }

    RequestResponseLogCollector getLog() {
        return collector;
    }

    void turnOffReportingOftLog() {
        this.toReportLog = false;
    }
}
