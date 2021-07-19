package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import retrofit2.Call;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectSupplier<M, R> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<R>, GetObjectSupplier<M, R>> {

    private final SendRequestAndGet<M, R> delegateTo;

    protected GetObjectSupplier(Supplier<M> call, Function<M, R> f) {
        super(RequestExecutionResult::getResult);
        this.delegateTo = getResponse(new GetStepResultFunction<>(f)).from(call);
        from(this.delegateTo);
    }

    @Description("{description}")
    public static <M, R> GetObjectSupplier<M, R> object(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectSupplier<>(call, f);
    }

    @Description("Response body")
    public static <M> GetObjectSupplier<M, M> body(Supplier<M> call) {
        return new GetObjectSupplier<>(call, m -> m);
    }

    public static <M, R> GetObjectSupplier<M, R> callObject(String description,
                                                            Supplier<Call<M>> call,
                                                            Function<M, R> f) {
        return object(description, new CallBodySupplier<>(call), f);
    }

    @Description("Response body")
    public static <M> GetObjectSupplier<M, M> callBody(Supplier<Call<M>> call) {
        return new GetObjectSupplier<>(new CallBodySupplier<>(call), m -> m);
    }

    public GetObjectSupplier<M, R> retryTimeOut(Duration timeOut) {
        delegateTo.timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectSupplier<M, R> pollingInterval(Duration timeOut) {
        delegateTo.pollingInterval(timeOut);
        return this;
    }

    public GetObjectSupplier<M, R> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetObjectSupplier<M, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetObjectSupplier<M, R> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(criteria.toString(), r -> criteria.get().test(r)));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectSupplier<M, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectSupplier<M, R> throwOnNoResult() {
        delegateTo.throwOnNoResult();
        return this;
    }
}
