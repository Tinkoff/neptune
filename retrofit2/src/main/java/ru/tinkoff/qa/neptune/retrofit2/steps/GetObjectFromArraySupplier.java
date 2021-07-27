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
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.arrayBodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.resultResponseCriteria;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectFromArraySupplier<M, R> extends SequentialGetStepSupplier
        .GetObjectFromArrayChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<R[]>, GetObjectFromArraySupplier<M, R>> {

    protected GetObjectFromArraySupplier(Supplier<M> call, Function<M, R[]> f) {
        super(RequestExecutionResult::getResult);
        from(getResponse(new GetStepResultFunction<>(f)).from(call));
    }

    @Description("{description}")
    public static <M, R> GetObjectFromArraySupplier<M, R> arrayItem(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromArraySupplier<>(call, f);
    }

    public static <M> GetObjectFromArraySupplier<M[], M> arrayItem(String description,
                                                                   Supplier<M[]> call) {
        return arrayItem(description, call, ms -> ms);
    }

    public static <M, R> GetObjectFromArraySupplier<M, R> callArrayItem(String description,
                                                                        Supplier<Call<M>> call, Function<M, R[]> f) {
        return arrayItem(description, new CallBodySupplier<>(call), f);
    }

    public static <M> GetObjectFromArraySupplier<M[], M> callArrayItem(String description,
                                                                       Supplier<Call<M[]>> call) {
        return callArrayItem(description, call, ms -> ms);
    }

    public GetObjectFromArraySupplier<M, R> retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, R[]>) getFrom()).timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectFromArraySupplier<M, R> pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, R[]>) getFrom()).pollingInterval(timeOut);
        return this;
    }

    public GetObjectFromArraySupplier<M, R> responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(criteria));
        return this;
    }

    public GetObjectFromArraySupplier<M, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetObjectFromArraySupplier<M, R> criteria(Criteria<? super R> criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(arrayBodyMatches(new BodyHasItems(criteria), criteria));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectFromArraySupplier<M, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectFromArraySupplier<M, R> throwOnNoResult() {
        ((SendRequestAndGet<M, R[]>) getFrom()).throwOnNoResult();
        return this;
    }
}
