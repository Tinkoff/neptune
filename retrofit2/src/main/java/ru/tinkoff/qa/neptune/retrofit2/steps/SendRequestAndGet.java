package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;

import java.time.Duration;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;


@Description("Http response")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
@ThrowWhenNoData(toThrow = ExpectedHttpResponseHasNotBeenReceivedException.class, startDescription = "Not received")
class SendRequestAndGet<M, R> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<RetrofitContext, RequestExecutionResult<R>, Supplier<M>, SendRequestAndGet<M, R>> {

    private final StepExecutionHook hook;

    private SendRequestAndGet(GetStepResultFunction<M, R> f) {
        super(f);
        hook = new StepExecutionHook(f);
        addIgnored(Exception.class);
    }

    static <T, R> SendRequestAndGet<T, R> getResponse(GetStepResultFunction<T, R> f) {
        return new SendRequestAndGet<>(f);
    }

    @Override
    protected Map<String, String> additionalParameters() {
        return hook.getRequestParameters();
    }

    @Override
    protected void onSuccess(RequestExecutionResult<R> rRequestExecutionResult) {
        hook.onSuccess();
    }

    @Override
    protected void onFailure(Supplier<M> supplier, Throwable throwable) {
        hook.onFailure();
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

    @Override
    protected SendRequestAndGet<M, R> criteria(String description, Predicate<? super RequestExecutionResult<R>> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected SendRequestAndGet<M, R> criteria(Criteria<? super RequestExecutionResult<R>> criteria) {
        return super.criteria(criteria);
    }
}