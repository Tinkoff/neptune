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
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.retrofit2.steps.BodyMatches.body;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.*;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultHasItems.hasResultItems;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted array")
public abstract class GetArraySupplier<M, R, S extends GetArraySupplier<M, R, S>> extends SequentialGetStepSupplier
        .GetArrayChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<M, R[]>, S>
        implements DefinesResponseCriteria<S> {

    private Criteria<R> derivedValueCriteria;

    protected GetArraySupplier(SendRequestAndGet<M, R[]> from) {
        super(RequestExecutionResult::getResult);
        from(from);
    }

    /**
     * Creates a step that gets some array value which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of item of array
     * @return an instance of {@link ChainedGetArraySupplier}
     */
    @Description("{description}")
    public static <M, R> ChainedGetArraySupplier<M, R> array(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new ChainedGetArraySupplier<>(getResponse(translate(description),
                new GetStepResultFunction<>(f, rs -> nonNull(rs) && rs.length > 0))
                .from(call));
    }

    /**
     * Creates a step that gets some array value from array body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <M>         is a type of item of array
     * @return an instance of {@link SimpleGetArraySupplier}
     */
    @Description("{description}")
    public static <M> SimpleGetArraySupplier<M> array(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M[]> call) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new SimpleGetArraySupplier<>(getResponse(translate(description),
                new GetStepResultFunction<>((Function<M[], M[]>) ms -> ms, ms -> nonNull(ms) && ms.length > 0))
                .from(call));
    }

    /**
     * Creates a step that gets some array value which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of item of array
     * @return an instance of {@link ChainedGetArraySupplier}
     */
    public static <M, R> ChainedGetArraySupplier<M, R> callArray(String description, Supplier<Call<M>> call, Function<M, R[]> f) {
        return array(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets some array value from array body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param <M>         is a type of item of array
     * @return an instance of {@link SimpleGetArraySupplier}
     */
    public static <M> SimpleGetArraySupplier<M> callArray(String description, Supplier<Call<M[]>> call) {
        return array(description, new CallBodySupplier<>(call));
    }

    @Override
    public S retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, R[]>) getFrom()).timeOut(timeOut);
        return (S) this;
    }

    @Override
    public S pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, R[]>) getFrom()).pollingInterval(timeOut);
        return (S) this;
    }

    @Override
    public S responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(criteria));
        return (S) this;
    }

    @Override
    public final S responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    @SafeVarargs
    public final S responseCriteriaOr(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(OR(criteria)));
        return (S) this;
    }

    @Override
    @SafeVarargs
    public final S responseCriteriaOnlyOne(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(ONLY_ONE(criteria)));
        return (S) this;
    }

    @Override
    @SafeVarargs
    public final S responseCriteriaNot(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R[]>) getFrom()).criteria(resultResponseCriteria(NOT(criteria)));
        return (S) this;
    }

    private void criteriaForDerivedValue(Criteria<? super R> criteria) {
        derivedValueCriteria = ofNullable(derivedValueCriteria)
                .map(c -> AND(c, criteria))
                .orElse((Criteria<R>) criteria);
    }

    @Override
    public S criteriaOr(Criteria<? super R>... criteria) {
        criteriaForDerivedValue(OR(criteria));
        return super.criteriaOr(criteria);
    }

    @Override
    public S criteriaOnlyOne(Criteria<? super R>... criteria) {
        criteriaForDerivedValue(ONLY_ONE(criteria));
        return super.criteriaOnlyOne(criteria);
    }

    @Override
    public S criteriaNot(Criteria<? super R>... criteria) {
        criteriaForDerivedValue(NOT(criteria));
        return super.criteriaNot(criteria);
    }

    @Override
    public S criteria(Criteria<? super R> criteria) {
        criteriaForDerivedValue(criteria);
        return super.criteria(criteria);
    }

    @Override
    public S criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(description, criteria));
    }

    @Override
    public S throwOnNoResult() {
        ((SendRequestAndGet<M, R[]>) getFrom()).throwOnNoResult();
        super.throwOnNoResult();
        return (S) this;
    }

    @Override
    public Function<RetrofitContext, R[]> get() {
        if (derivedValueCriteria != null) {
            ((SendRequestAndGet<M, R[]>) getFrom()).criteria(arrayResultMatches(hasResultItems(derivedValueCriteria)));
        }
        return super.get();
    }

    public static class SimpleGetArraySupplier<M> extends GetArraySupplier<M[], M, GetArraySupplier.SimpleGetArraySupplier<M>> {

        private SimpleGetArraySupplier(SendRequestAndGet<M[], M[]> from) {
            super(from);
        }
    }

    public static class ChainedGetArraySupplier<M, R> extends GetArraySupplier<M, R, GetArraySupplier.ChainedGetArraySupplier<M, R>> {

        private ChainedGetArraySupplier(SendRequestAndGet<M, R[]> from) {
            super(from);
        }

        public ChainedGetArraySupplier<M, R> callBodyCriteria(Criteria<? super M> criteria) {
            ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(body(criteria)));
            return this;
        }

        public ChainedGetArraySupplier<M, R> callBodyCriteria(String description, Predicate<? super M> predicate) {
            return callBodyCriteria(condition(description, predicate));
        }

        public ChainedGetArraySupplier<M, R> callBodyCriteriaOr(Criteria<? super M>... criteria) {
            return callBodyCriteria(OR(criteria));
        }

        public ChainedGetArraySupplier<M, R> callBodyCriteriaOnlyOne(Criteria<? super M>... criteria) {
            return callBodyCriteria(ONLY_ONE(criteria));
        }

        public ChainedGetArraySupplier<M, R> callBodyCriteriaNot(Criteria<? super M>... criteria) {
            return callBodyCriteria(NOT(criteria));
        }
    }
}
