package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.captors.request.AbstractRequestBodyCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectsCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.RequestResponseLogCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.ResponseCaptor;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;

@MaxDepthOfReporting(1)
@ThrowWhenNoData(toThrow = ExpectedHttpResponseHasNotBeenReceivedException.class, startDescription = "Not received")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
@Description("Http Response")
final class ResponseSequentialGetSupplierInternal<T> extends SequentialGetStepSupplier
        .GetObjectStepSupplier<HttpStepContext, HttpResponse<T>, ResponseSequentialGetSupplierInternal<T>> {

    final RequestParameters parameters;
    private final GetResponseFunction<T> f;
    @CaptureOnSuccess(by = RequestResponseLogCaptor.class)
    @CaptureOnFailure(by = RequestResponseLogCaptor.class)
    ResponseExecutionInfo info;

    @CaptureOnSuccess(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
    @CaptureOnFailure(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
    HttpResponse<?> lastReceived;

    private ResponseSequentialGetSupplierInternal(GetResponseFunction<T> f) {
        super(f);
        this.f = f;
        parameters = new RequestParameters(f.getRequest());
    }

    static <T> ResponseSequentialGetSupplierInternal<T> responseInternal(RequestBuilder requestBuilder,
                                                                         HttpResponse.BodyHandler<T> bodyHandler) {
        return new ResponseSequentialGetSupplierInternal<>(new GetResponseFunction<>(requestBuilder.build(), bodyHandler));
    }

    @Override
    protected ResponseSequentialGetSupplierInternal<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected ResponseSequentialGetSupplierInternal<T> pollingInterval(Duration timeOut) {
        return super.pollingInterval(timeOut);
    }

    @Override
    protected ResponseSequentialGetSupplierInternal<T> addIgnored(Class<? extends Throwable> toBeIgnored) {
        return super.addIgnored(toBeIgnored);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(HttpStepContext httpStepContext) {
        if (toReport) {
            catchValue(f.getRequest().body(), createCaptors(new Class[]{AbstractRequestBodyCaptor.class}));
        }
        info = f.getInfo();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSuccess(HttpResponse<T> tHttpResponse) {
        lastReceived = ofNullable(tHttpResponse).orElseGet(() -> (HttpResponse<T>) info.getLastReceived());
    }

    @Override
    protected void onFailure(HttpStepContext httpStepContext, Throwable throwable) {
        lastReceived = info.getLastReceived();
    }
}
