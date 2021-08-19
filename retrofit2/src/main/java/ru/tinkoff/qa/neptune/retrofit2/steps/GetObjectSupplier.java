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
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.resultResponseCriteria;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectSupplier<M, R> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<R>, GetObjectSupplier<M, R>> {

    protected GetObjectSupplier(Supplier<M> call, Function<M, R> f) {
        super(RequestExecutionResult::getResult);
        from(getResponse(new GetStepResultFunction<>(f)).from(call));
    }

    /**
     * Creates a step that gets some value from object which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an object
     * @return an instance of {@link GetObjectSupplier}
     */
    @Description("{description}")
    public static <M, R> GetObjectSupplier<M, R> object(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectSupplier<>(call, f);
    }

    /**
     * Creates a step that gets body of synchronous http call.
     *
     * @param call describes a single synchronous call
     * @param <M>  deserialized body
     * @return an instance of {@link GetObjectSupplier}
     */
    @Description("Response body")
    public static <M> GetObjectSupplier<M, M> body(Supplier<M> call) {
        return new GetObjectSupplier<>(call, m -> m);
    }

    /**
     * Creates a step that gets some value from object which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an object
     * @return an instance of {@link GetObjectSupplier}
     */
    public static <M, R> GetObjectSupplier<M, R> callObject(String description,
                                                            Supplier<Call<M>> call,
                                                            Function<M, R> f) {
        return object(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets body of http call.
     *
     * @param call describes a single synchronous call
     * @param <M>  deserialized body
     * @return an instance of {@link GetObjectSupplier}
     */
    @Description("Response body")
    public static <M> GetObjectSupplier<M, M> callBody(Supplier<Call<M>> call) {
        return new GetObjectSupplier<>(new CallBodySupplier<>(call), m -> m);
    }

    public GetObjectSupplier<M, R> retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, R>) getFrom()).timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectSupplier<M, R> pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, R>) getFrom()).pollingInterval(timeOut);
        return this;
    }

    public GetObjectSupplier<M, R> responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(criteria));
        return this;
    }

    public GetObjectSupplier<M, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    public GetObjectSupplier<M, R> responseCriteriaOr(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(OR(criteria)));
        return this;
    }

    public GetObjectSupplier<M, R> responseCriteriaOnlyOne(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(ONLY_ONE(criteria)));
        return this;
    }

    public GetObjectSupplier<M, R> responseCriteriaNot(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(NOT(criteria)));
        return this;
    }

    @Override
    public GetObjectSupplier<M, R> criteriaOr(Criteria<? super R>... criteria) {
        var orCriteria = OR(criteria);
        ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(new BodyMatches(orCriteria), orCriteria));
        return super.criteriaOr(criteria);
    }

    @Override
    public GetObjectSupplier<M, R> criteriaOnlyOne(Criteria<? super R>... criteria) {
        var xorCriteria = ONLY_ONE(criteria);
        ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(new BodyMatches(xorCriteria), xorCriteria));
        return super.criteriaOnlyOne(criteria);
    }

    @Override
    public GetObjectSupplier<M, R> criteriaNot(Criteria<? super R>... criteria) {
        var notCriteria = NOT(criteria);
        ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(new BodyMatches(notCriteria), notCriteria));
        return super.criteriaNot(criteria);
    }

    @Override
    public GetObjectSupplier<M, R> criteria(Criteria<? super R> criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(new BodyMatches(criteria), criteria));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectSupplier<M, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectSupplier<M, R> throwOnNoResult() {
        ((SendRequestAndGet<M, R>) getFrom()).throwOnNoResult();
        super.throwOnNoResult();
        return this;
    }
}
