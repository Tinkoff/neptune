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
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.getCaptors;

/**
 * Builds a step-function that receives http response
 *
 * @param <T> is a type of response body
 */
@Description("Http Response")
@MaxDepthOfReporting(1)
@ThrowWhenNoData(toThrow = ExpectedHttpResponseHasNotBeenReceivedException.class, startDescription = "Not received")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
public final class ResponseSequentialGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<HttpStepContext, HttpResponse<T>,
    ResponseSequentialGetSupplier<T>> {

    final RequestParameters parameters;

    @CaptureOnSuccess(by = RequestResponseLogCaptor.class)
    @CaptureOnFailure(by = RequestResponseLogCaptor.class)
    final ResponseExecutionInfo info;

    private final NeptuneHttpRequestImpl request;

    @CaptureOnSuccess(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
    @CaptureOnFailure(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
    HttpResponse<?> lastReceived;

    private <R> ResponseSequentialGetSupplier(NeptuneHttpRequestImpl request,
                                              HttpResponse.BodyHandler<T> bodyHandler,
                                              Function<T, R> additionalCalculation,
                                              ResponseExecutionInfo info) {
        super(httpStepContext -> {
            info.setLastReceived(null);
            try {
                var response = httpStepContext.getCurrentClient().send(request, bodyHandler);
                info.setLastReceived(response);
                var calculated = additionalCalculation.apply(response.body());
                return new Response<>(response, calculated);
            } catch (Exception e) {
                throw new HttpResponseException(e);
            }
        });
        checkNotNull(request);
        this.request = request;
        this.info = info;
        parameters = new RequestParameters(request);
    }

    /**
     * Creates an instance that builds a step-function to send http request and to receive a response.
     *
     * @param requestBuilder is a builder of http request
     * @param calculation    is an additional calculation to get some value from response body
     * @param <T>            is a type of response body
     * @param <R>            is a type of value calculated using response body
     * @return an instance of {@link ResponseSequentialGetSupplier}
     */
    public static <T, R> ResponseSequentialGetSupplier<T> response(RequestBuilder<T> requestBuilder,
                                                                   Function<T, R> calculation) {
        checkNotNull(requestBuilder);
        checkNotNull(calculation);
        return new ResponseSequentialGetSupplier<>(
            (NeptuneHttpRequestImpl) requestBuilder.build(),
            ((RequestBuilderFactory.RequestBuilderWithHandler<T>) requestBuilder).getBodyHandler(),
            calculation,
            new ResponseExecutionInfo());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(HttpStepContext httpStepContext) {
        info.startExecutionLogging();
        if (toReport) {
            catchValue(request.body(), getCaptors(new Class[]{AbstractRequestBodyCaptor.class}));
        }
    }

    @Override
    protected void onSuccess(HttpResponse<T> result) {
        afterExecution();
    }

    @Override
    protected void onFailure(HttpStepContext httpStepContext, Throwable throwable) {
        afterExecution();
    }

    private void afterExecution() {
        info.stopExecutionLogging();
        lastReceived = info.getLastReceived();
    }

    @Override
    protected ResponseSequentialGetSupplier<T> timeOut(Duration duration) {
        return super.timeOut(duration);
    }

    @Override
    protected ResponseSequentialGetSupplier<T> pollingInterval(Duration duration) {
        return super.pollingInterval(duration);
    }

    @Override
    protected ResponseSequentialGetSupplier<T> addIgnored(Class<? extends Throwable> toBeIgnored) {
        return super.addIgnored(toBeIgnored);
    }
}