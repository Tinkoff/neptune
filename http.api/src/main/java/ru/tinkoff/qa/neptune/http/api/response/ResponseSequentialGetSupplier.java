package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.captors.request.AbstractRequestBodyCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectsCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.RequestResponseLogCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.ResponseCaptor;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;

import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;

/**
 * Builds a step-function that receives http response
 *
 * @param <T> is a type of response body
 */
@CaptureOnSuccess(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
@MaxDepthOfReporting(0)
@Description("Http Response")
public final class ResponseSequentialGetSupplier<T> extends SequentialGetStepSupplier.GetSimpleStepSupplier<HttpStepContext, HttpResponse<T>,
        ResponseSequentialGetSupplier<T>> {

    final RequestParameters parameters;
    @CaptureOnSuccess(by = RequestResponseLogCaptor.class)
    @CaptureOnFailure(by = RequestResponseLogCaptor.class)
    final ResponseExecutionInfo info;
    private final GetResponseFunction<T, T> f;

    private ResponseSequentialGetSupplier(GetResponseFunction<T, T> f) {
        super(f.andThen(ResponseExecutionResult::getResponse));
        this.f = f;
        info = f.getInfo();
        parameters = new RequestParameters(f.getRequest());
    }

    /**
     * Creates an instance that builds a step-function to send an http request and to receive a response
     * with body.
     *
     * @param requestBuilder is a builder of an http request
     * @param bodyHandler    of a response body
     * @param <T>            is a type of response body
     * @return an instance of {@link ResponseSequentialGetSupplier}
     */
    public static <T> ResponseSequentialGetSupplier<T> response(RequestBuilder requestBuilder,
                                                                HttpResponse.BodyHandler<T> bodyHandler) {
        return new ResponseSequentialGetSupplier<>(new GetResponseFunction<>(requestBuilder.build(), bodyHandler, t -> t));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(HttpStepContext httpStepContext) {
        if (toReport) {
            catchValue(f.getRequest().body(), createCaptors(new Class[]{AbstractRequestBodyCaptor.class}));
        }
    }
}
