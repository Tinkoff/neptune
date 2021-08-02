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
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted array")
public class GetArraySupplier<M, R> extends SequentialGetStepSupplier
        .GetArrayChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<R[]>, GetArraySupplier<M, R>> {

    protected GetArraySupplier(Supplier<M> call, Function<M, R[]> f) {
        super(RequestExecutionResult::getResult);
        from(getResponse(new GetStepResultFunction<>(f)).from(call));
    }

    /**
     * Creates a step that gets some array value which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an item of array
     * @return an instance of {@link GetArraySupplier}
     */
    @Description("{description}")
    public static <M, R> GetArraySupplier<M, R> array(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetArraySupplier<>(call, f);
    }

    /**
     * Creates a step that gets some array value from array body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <M>         is a type of an item of array
     * @return an instance of {@link GetArraySupplier}
     */
    public static <M> GetArraySupplier<M[], M> array(String description, Supplier<M[]> call) {
        return array(description, call, ms -> ms);
    }

    /**
     * Creates a step that gets some array value which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an item of array
     * @return an instance of {@link GetArraySupplier}
     */
    public static <M, R> GetArraySupplier<M, R> callArray(String description, Supplier<Call<M>> call, Function<M, R[]> f) {
        return array(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets some array value from array body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param <M>         is a type of an item of array
     * @return an instance of {@link GetArraySupplier}
     */
    public static <M> GetArraySupplier<M[], M> callArray(String description, Supplier<Call<M[]>> call) {
        return callArray(description, call, ms -> ms);
    }

    public GetArraySupplier<M, R> retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, R[]>) getFrom()).timeOut(timeOut);
        return this;
    }

    @Override
    public GetArraySupplier<M, R> pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, R[]>) getFrom()).pollingInterval(timeOut);
        return this;
    }

    public GetArraySupplier<M, R> responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(criteria));
        return this;
    }

    public GetArraySupplier<M, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetArraySupplier<M, R> criteria(Criteria<? super R> criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(arrayBodyMatches(new BodyHasItems(criteria), criteria));
        return super.criteria(criteria);
    }

    @Override
    public GetArraySupplier<M, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetArraySupplier<M, R> throwOnNoResult() {
        ((SendRequestAndGet<M, R[]>) getFrom()).throwOnNoResult();
        return this;
    }
}
