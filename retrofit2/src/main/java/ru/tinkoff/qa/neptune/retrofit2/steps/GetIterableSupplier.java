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
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.*;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public class GetIterableSupplier<M, R, S extends Iterable<R>> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<RetrofitContext, S, RequestExecutionResult<S>, R, GetIterableSupplier<M, R, S>> {

    protected GetIterableSupplier(Supplier<M> call, Function<M, S> f) {
        super(RequestExecutionResult::getResult);
        from(getResponse(new GetStepResultFunction<>(f)).from(call));
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetIterableSupplier}
     */
    @Description("{description}")
    public static <M, R, S extends Iterable<R>> GetIterableSupplier<M, R, S> iterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetIterableSupplier<>(call, f);
    }

    /**
     * Creates a step that gets some iterable value from iterable body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetIterableSupplier}
     */
    public static <R, S extends Iterable<R>> GetIterableSupplier<S, R, S> iterable(String description,
                                                                                   Supplier<S> call) {
        return iterable(description, call, rs -> rs);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetIterableSupplier}
     */
    public static <M, R, S extends Iterable<R>> GetIterableSupplier<M, R, S> callIterable(String description,
                                                                                          Supplier<Call<M>> call, Function<M, S> f) {
        return iterable(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets some iterable value from iterable body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetIterableSupplier}
     */
    public static <R, S extends Iterable<R>> GetIterableSupplier<S, R, S> callIterable(String description,
                                                                                       Supplier<Call<S>> call) {
        return callIterable(description, call, rs -> rs);
    }

    public GetIterableSupplier<M, R, S> retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, S>) getFrom()).timeOut(timeOut);
        return this;
    }

    @Override
    public GetIterableSupplier<M, R, S> pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, S>) getFrom()).pollingInterval(timeOut);
        return this;
    }

    public GetIterableSupplier<M, R, S> responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, S>) getFrom()).criteria(resultResponseCriteria(criteria));
        return this;
    }

    public GetIterableSupplier<M, R, S> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    public GetIterableSupplier<M, R, S> responseCriteriaOr(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(OR(criteria)));
        return this;
    }

    public GetIterableSupplier<M, R, S> responseCriteriaOnlyOne(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(ONLY_ONE(criteria)));
        return this;
    }

    public GetIterableSupplier<M, R, S> responseCriteriaNot(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(NOT(criteria)));
        return this;
    }

    @Override
    public GetIterableSupplier<M, R, S> criteriaOr(Criteria<? super R>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteriaOr(arrayBodyMatches(new BodyHasItems(OR(criteria)), OR(criteria)));
        return super.criteriaOr(criteria);
    }

    @Override
    public GetIterableSupplier<M, R, S> criteriaOnlyOne(Criteria<? super R>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteriaOnlyOne(arrayBodyMatches(new BodyHasItems(ONLY_ONE(criteria)), ONLY_ONE(criteria)));
        return super.criteriaOnlyOne(criteria);
    }

    @Override
    public GetIterableSupplier<M, R, S> criteriaNot(Criteria<? super R>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteriaNot(arrayBodyMatches(new BodyHasItems(NOT(criteria)), NOT(criteria)));
        return super.criteriaNot(criteria);
    }

    @Override
    public GetIterableSupplier<M, R, S> criteria(Criteria<? super R> criteria) {
        ((SendRequestAndGet<M, S>) getFrom()).criteria(iterableBodyMatches(new BodyHasItems(criteria), criteria));
        return super.criteria(criteria);
    }

    @Override
    public GetIterableSupplier<M, R, S> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetIterableSupplier<M, R, S> throwOnNoResult() {
        ((SendRequestAndGet<M, S>) getFrom()).throwOnNoResult();
        return this;
    }
}
