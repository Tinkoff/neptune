package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;

import java.time.Duration;
import java.util.Map;
import java.util.function.Predicate;


@Description("Http response")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
@ThrowWhenNoData(toThrow = ExpectedHttpResponseHasNotBeenReceivedException.class, startDescription = "Not received")
class SendRequestAndGet<T, R> extends SequentialGetStepSupplier
        .GetObjectStepSupplier<RetrofitContext<T>, RequestExecutionResult<R>, SendRequestAndGet<T, R>> {

    private final StepExecutionHook hook;

    SendRequestAndGet(GetStepResultFunction<T, R> f) {
        super(f);
        hook = new StepExecutionHook(f);
        addIgnored(Exception.class);
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
    protected void onFailure(RetrofitContext<T> tRetrofitContext, Throwable throwable) {
        hook.onFailure();
    }

    @Override
    protected SendRequestAndGet<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected SendRequestAndGet<T, R> criteria(String description, Predicate<? super RequestExecutionResult<R>> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected SendRequestAndGet<T, R> criteria(Criteria<? super RequestExecutionResult<R>> criteria) {
        return super.criteria(criteria);
    }
}