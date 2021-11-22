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
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.size;
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
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public abstract class GetIterableSupplier<M, R, S extends Iterable<R>, E extends GetIterableSupplier<M, R, S, E>>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<RetrofitContext, S, RequestExecutionResult<M, S>, R, E>
        implements DefinesResponseCriteria<E> {

    private Criteria<R> derivedValueCriteria;

    protected GetIterableSupplier(SendRequestAndGet<M, S> from) {
        super(RequestExecutionResult::getResult);
        from(from);
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link ChainedGetIterableSupplier}
     */
    @Description("{description}")
    public static <M, R, S extends Iterable<R>> ChainedGetIterableSupplier<M, R, S> iterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new ChainedGetIterableSupplier<>(getResponse(translate(description),
                new GetStepResultFunction<>(f, rs -> nonNull(rs) && size(rs) > 0))
                .from(call));
    }

    /**
     * Creates a step that gets some iterable value from iterable body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <R>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link SimpleGetIterableSupplier}
     */
    @Description("{description}")
    public static <R, S extends Iterable<R>> SimpleGetIterableSupplier<R, S> iterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<S> call) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new SimpleGetIterableSupplier<>(getResponse(translate(description),
                new GetStepResultFunction<>((Function<S, S>) rs -> rs, rs -> nonNull(rs) && size(rs) > 0))
                .from(call));
    }

    /**
     * Creates a step that gets some iterable value which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link ChainedGetIterableSupplier}
     */
    public static <M, R, S extends Iterable<R>> ChainedGetIterableSupplier<M, R, S> callIterable(String description,
                                                                                                 Supplier<Call<M>> call, Function<M, S> f) {
        return iterable(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets some iterable value from iterable body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single call
     * @param <R>         is a type of item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link SimpleGetIterableSupplier}
     */
    public static <R, S extends Iterable<R>> SimpleGetIterableSupplier<R, S> callIterable(String description,
                                                                                          Supplier<Call<S>> call) {
        return iterable(description, new CallBodySupplier<>(call));
    }

    @Override
    public E retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, S>) getFrom()).timeOut(timeOut);
        return (E) this;
    }

    @Override
    public E pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, S>) getFrom()).pollingInterval(timeOut);
        return (E) this;
    }

    @Override
    public E responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, S>) getFrom()).criteria(resultResponseCriteria(criteria));
        return (E) this;
    }

    @Override
    public E responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    @SafeVarargs
    public final E responseCriteriaOr(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, S>) getFrom()).criteria(resultResponseCriteria(OR(criteria)));
        return (E) this;
    }

    @Override
    @SafeVarargs
    public final E responseCriteriaOnlyOne(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, S>) getFrom()).criteria(resultResponseCriteria(ONLY_ONE(criteria)));
        return (E) this;
    }

    @Override
    @SafeVarargs
    public final E responseCriteriaNot(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, S>) getFrom()).criteria(resultResponseCriteria(NOT(criteria)));
        return (E) this;
    }

    private void criteriaForDerivedValue(Criteria<? super R> criteria) {
        derivedValueCriteria = ofNullable(derivedValueCriteria)
                .map(c -> AND(c, criteria))
                .orElse((Criteria<R>) criteria);
    }

    @Override
    public E criteriaOr(Criteria<? super R>... criteria) {
        criteriaForDerivedValue(OR(criteria));
        return super.criteriaOr(criteria);
    }

    @Override
    public E criteriaOnlyOne(Criteria<? super R>... criteria) {
        criteriaForDerivedValue(ONLY_ONE(criteria));
        return super.criteriaOnlyOne(criteria);
    }

    @Override
    public E criteriaNot(Criteria<? super R>... criteria) {
        criteriaForDerivedValue(NOT(criteria));
        return super.criteriaNot(criteria);
    }

    @Override
    public E criteria(Criteria<? super R> criteria) {
        criteriaForDerivedValue(criteria);
        return super.criteria(criteria);
    }

    @Override
    public E criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(description, criteria));
    }

    @Override
    public E throwOnNoResult() {
        ((SendRequestAndGet<M, S>) getFrom()).throwOnNoResult();
        super.throwOnNoResult();
        return (E) this;
    }

    @Override
    public Function<RetrofitContext, List<R>> get() {
        if (derivedValueCriteria != null) {
            ((SendRequestAndGet<M, S>) getFrom()).criteria(iterableResultMatches(hasResultItems(derivedValueCriteria)));
        }
        return super.get();
    }

    public static class SimpleGetIterableSupplier<R, S extends Iterable<R>> extends GetIterableSupplier<S, R, S, SimpleGetIterableSupplier<R, S>> {

        private SimpleGetIterableSupplier(SendRequestAndGet<S, S> from) {
            super(from);
        }
    }

    public static class ChainedGetIterableSupplier<M, R, S extends Iterable<R>> extends GetIterableSupplier<M, R, S, ChainedGetIterableSupplier<M, R, S>> {

        private ChainedGetIterableSupplier(SendRequestAndGet<M, S> from) {
            super(from);
        }

        public ChainedGetIterableSupplier<M, R, S> callBodyCriteria(Criteria<? super M> criteria) {
            ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(body(criteria)));
            return this;
        }

        public ChainedGetIterableSupplier<M, R, S> callBodyCriteria(String description, Predicate<? super M> predicate) {
            return callBodyCriteria(condition(description, predicate));
        }

        public ChainedGetIterableSupplier<M, R, S> callBodyCriteriaOr(Criteria<? super M>... criteria) {
            return callBodyCriteria(OR(criteria));
        }

        public ChainedGetIterableSupplier<M, R, S> callBodyCriteriaOnlyOne(Criteria<? super M>... criteria) {
            return callBodyCriteria(ONLY_ONE(criteria));
        }

        public ChainedGetIterableSupplier<M, R, S> callBodyCriteriaNot(Criteria<? super M>... criteria) {
            return callBodyCriteria(NOT(criteria));
        }
    }
}
