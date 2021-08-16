package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;
import ru.tinkoff.qa.neptune.retrofit2.captors.AbstractRequestBodyCaptor;
import ru.tinkoff.qa.neptune.retrofit2.captors.MultipartRequestBodyCaptor;
import ru.tinkoff.qa.neptune.retrofit2.captors.ResponseBodyCaptor;
import ru.tinkoff.qa.neptune.retrofit2.captors.ResponseCaptor;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;


@Description("Http response")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
@ThrowWhenNoData(toThrow = ExpectedHttpResponseHasNotBeenReceivedException.class, startDescription = "Not received")
class SendRequestAndGet<M, R> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<RetrofitContext, RequestExecutionResult<R>, Supplier<M>, SendRequestAndGet<M, R>> {

    private final GetStepResultFunction<M, R> f;

    @CaptureOnSuccess(by = ResponseCaptor.class)
    @CaptureOnFailure(by = ResponseCaptor.class)
    Response response;

    @CaptureOnSuccess(by = ResponseBodyCaptor.class)
    @CaptureOnFailure(by = ResponseBodyCaptor.class)
    ResponseBody body;

    private SendRequestAndGet(GetStepResultFunction<M, R> f) {
        super(f);
        this.f = f;
        addIgnored(Exception.class);
    }

    static <T, R> SendRequestAndGet<T, R> getResponse(GetStepResultFunction<T, R> f) {
        return new SendRequestAndGet<>(f);
    }

    @Override
    protected Map<String, String> additionalParameters() {
        var r = f.request();
        if (r != null) {
            var result = new LinkedHashMap<String, String>();
            result.put("URL", r.url().toString());
            result.put("METHOD", r.method());
            var h = r.headers();
            var headerMap = h.toMultimap();

            headerMap.forEach((k, v) -> result.put("Header " + k, String.join(",", v)));
            return result;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void fillResultData() {
        var r = f.request();
        if (r != null) {
            catchValue(r.body(), createCaptors(new Class[]{AbstractRequestBodyCaptor.class, MultipartRequestBodyCaptor.class}));
        }

        response = f.response();
        body = f.body();
    }

    @Override
    protected void onSuccess(RequestExecutionResult<R> rRequestExecutionResult) {
        fillResultData();
    }

    @Override
    protected void onFailure(Supplier<M> supplier, Throwable throwable) {
        fillResultData();
    }

    @Override
    protected SendRequestAndGet<M, R> from(Supplier<M> from) {
        return super.from(from);
    }

    @Override
    protected SendRequestAndGet<M, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected SendRequestAndGet<M, R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }
}