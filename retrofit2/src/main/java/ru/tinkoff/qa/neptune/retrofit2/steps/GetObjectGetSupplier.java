package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;

@Description("{description}")
@ThrowWhenNoData(startDescription = "Not received", toThrow = DataHasNotBeenReceivedException.class)
public class GetObjectGetSupplier<T, R> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<RetrofitContext<T>, R, RequestExecutionResult<R>, GetObjectGetSupplier<T, R>> {

    private final SendRequestAndGet<T, R> delegateTo;

    protected GetObjectGetSupplier(Function<T, R> f) {
        super(RequestExecutionResult::getResult);
        this.delegateTo = new SendRequestAndGet<>(new GetStepResultFunction<>(f));
        from(this.delegateTo);
    }

    public GetObjectGetSupplier<T, R> retryTimeOut(Duration timeOut) {
        delegateTo.timeOut(timeOut);
        return this;
    }

    public GetObjectGetSupplier<T, R> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetObjectGetSupplier<T, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    protected GetObjectGetSupplier<T, R> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(criteria.toString(), r -> criteria.get().test(r)));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectGetSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return criteria(condition(description, predicate));
    }

    @Description("Http response")
    @SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
    @ThrowWhenNoData(toThrow = ExpectedHttpResponseHasNotBeenReceivedException.class, startDescription = "Not received")
    @SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
    private static class SendRequestAndGet<T, R> extends SequentialGetStepSupplier
            .GetObjectStepSupplier<RetrofitContext<T>, RequestExecutionResult<R>, SendRequestAndGet<T, R>> {

        private final GetStepResultFunction<T, R> f;
        private final RequestReader requestReader = new RequestReader();

        protected SendRequestAndGet(GetStepResultFunction<T, R> f) {
            super(f);
            this.f = f;
        }

        @Override
        protected Map<String, String> additionalParameters() {
            return requestReader.getRequestParameters(f.request());
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
}
