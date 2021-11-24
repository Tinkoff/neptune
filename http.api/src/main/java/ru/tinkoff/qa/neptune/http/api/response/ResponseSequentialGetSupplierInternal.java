package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
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
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.getCaptors;

@MaxDepthOfReporting(1)
@ThrowWhenNoData(toThrow = ExpectedHttpResponseHasNotBeenReceivedException.class, startDescription = "Not received")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
final class ResponseSequentialGetSupplierInternal<T, R> extends SequentialGetStepSupplier
        .GetObjectStepSupplier<HttpStepContext, ResponseExecutionResult<T, R>, ResponseSequentialGetSupplierInternal<T, R>> {

    final RequestParameters parameters;
    @CaptureOnSuccess(by = RequestResponseLogCaptor.class)
    @CaptureOnFailure(by = RequestResponseLogCaptor.class)
    final ResponseExecutionInfo info;
    private final GetResponseFunction<T, R> f;

    @CaptureOnSuccess(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
    @CaptureOnFailure(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
    HttpResponse<?> lastReceived;

    private ResponseSequentialGetSupplierInternal(GetResponseFunction<T, R> f) {
        super(f);
        this.f = f;
        info = f.getInfo();
        parameters = new RequestParameters(f.getRequest());
    }

    @Description("Http Response. Body of the response is expected to have: '{description}'")
    static <T, R> ResponseSequentialGetSupplierInternal<T, R> responseInternal(@DescriptionFragment("description") String description,
                                                                               RequestBuilder requestBuilder,
                                                                               HttpResponse.BodyHandler<T> bodyHandler,
                                                                               Function<T, R> function,
                                                                               Predicate<R> predicate) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new ResponseSequentialGetSupplierInternal<>(new GetResponseFunction<>(requestBuilder.build(),
                bodyHandler,
                function, r -> r != null && predicate.test(r)));
    }

    @Description("Http Response")
    static <T> ResponseSequentialGetSupplierInternal<T, T> responseInternal(RequestBuilder requestBuilder,
                                                                            HttpResponse.BodyHandler<T> bodyHandler) {
        return new ResponseSequentialGetSupplierInternal<>(new GetResponseFunction<>(requestBuilder.build(),
                bodyHandler,
                t -> t, null));
    }

    @Override
    protected ResponseSequentialGetSupplierInternal<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected ResponseSequentialGetSupplierInternal<T, R> pollingInterval(Duration timeOut) {
        return super.pollingInterval(timeOut);
    }

    @Override
    protected ResponseSequentialGetSupplierInternal<T, R> addIgnored(Class<? extends Throwable> toBeIgnored) {
        return super.addIgnored(toBeIgnored);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(HttpStepContext httpStepContext) {
        if (toReport) {
            catchValue(f.getRequest().body(), getCaptors(new Class[]{AbstractRequestBodyCaptor.class}));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSuccess(ResponseExecutionResult<T, R> result) {
        lastReceived = ofNullable(result)
                .map(ResponseExecutionResult::getResponse)
                .orElseGet(() -> (HttpResponse<T>) info.getLastReceived());
    }

    @Override
    protected void onFailure(HttpStepContext httpStepContext, Throwable throwable) {
        lastReceived = info.getLastReceived();
    }
}
