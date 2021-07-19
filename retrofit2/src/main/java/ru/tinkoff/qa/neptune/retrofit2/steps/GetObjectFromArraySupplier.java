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
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectFromArraySupplier<M, R> extends SequentialGetStepSupplier
        .GetObjectFromArrayChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<R[]>, GetObjectFromArraySupplier<M, R>> {

    private final SendRequestAndGet<M, R[]> delegateTo;

    protected GetObjectFromArraySupplier(Supplier<M> call, Function<M, R[]> f) {
        super(RequestExecutionResult::getResult);
        this.delegateTo = getResponse(new GetStepResultFunction<>(f)).from(call);
        from(this.delegateTo);
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
        delegateTo.timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectFromArraySupplier<M, R> pollingInterval(Duration timeOut) {
        delegateTo.pollingInterval(timeOut);
        return this;
    }

    public GetObjectFromArraySupplier<M, R> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetObjectFromArraySupplier<M, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetObjectFromArraySupplier<M, R> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(new BodyHasItems(criteria.toString()).toString(),
                r -> stream(r).anyMatch(criteria.get())));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectFromArraySupplier<M, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectFromArraySupplier<M, R> throwOnNoResult() {
        delegateTo.throwOnNoResult();
        return this;
    }
}
