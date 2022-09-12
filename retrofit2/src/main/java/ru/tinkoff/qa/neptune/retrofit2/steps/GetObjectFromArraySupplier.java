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
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.retrofit2.steps.BodyMatches.body;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.resultResponseCriteria;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public abstract class GetObjectFromArraySupplier<M, R, S extends GetObjectFromArraySupplier<M, R, S>> extends SequentialGetStepSupplier
        .GetObjectFromArrayChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<M, R[]>, S>
        implements DefinesResponseCriteria<S> {

    protected GetObjectFromArraySupplier(SendRequestAndGet<M, R[]> from) {
        super(RequestExecutionResult::getResult);
        from(from);
    }

    /**
     * Creates a step that gets some value from array which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of item of array
     * @return an instance of {@link ChainedGetObjectFromArraySupplier}
     */
    @Description("{description}")
    public static <M, R> ChainedGetObjectFromArraySupplier<M, R> arrayItem(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new ChainedGetObjectFromArraySupplier<>(getResponse(translate(description),
                new GetStepResultFunction<>(f, rs -> nonNull(rs) && rs.length > 0))
                .from(call));
    }

    /**
     * Creates a step that gets some value from array body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <M>         deserialized body
     * @return an instance of {@link SimpleGetObjectFromArraySupplier}
     */
    @Description("{description}")
    public static <M> SimpleGetObjectFromArraySupplier<M> arrayItem(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M[]> call) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new SimpleGetObjectFromArraySupplier<>(getResponse(translate(description),
                new GetStepResultFunction<>((Function<M[], M[]>) ms -> ms, ms -> nonNull(ms) && ms.length > 0))
                .from(call));
    }

    /**
     * Creates a step that gets some value from array which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of item of array
     * @return an instance of {@link ChainedGetObjectFromArraySupplier}
     */
    public static <M, R> ChainedGetObjectFromArraySupplier<M, R> callArrayItem(String description,
                                                                               Supplier<Call<M>> call, Function<M, R[]> f) {
        return arrayItem(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets some value from array body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <M>         deserialized body
     * @return an instance of {@link GetObjectFromArraySupplier}
     */
    public static <M> SimpleGetObjectFromArraySupplier<M> callArrayItem(String description,
                                                                        Supplier<Call<M[]>> call) {
        return arrayItem(description, new CallBodySupplier<>(call));
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
    public S responseCriteria(String description, Predicate<Response> predicate) {
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

    @Override
    public S throwOnNoResult() {
        ((SendRequestAndGet<M, R[]>) getFrom()).throwOnNoResult();
        super.throwOnNoResult();
        return (S) this;
    }

    public static class SimpleGetObjectFromArraySupplier<M> extends GetObjectFromArraySupplier<M[], M, SimpleGetObjectFromArraySupplier<M>> {

        private SimpleGetObjectFromArraySupplier(SendRequestAndGet<M[], M[]> from) {
            super(from);
        }
    }

    public static class ChainedGetObjectFromArraySupplier<M, R> extends GetObjectFromArraySupplier<M, R, ChainedGetObjectFromArraySupplier<M, R>> {

        private ChainedGetObjectFromArraySupplier(SendRequestAndGet<M, R[]> from) {
            super(from);
        }

        public ChainedGetObjectFromArraySupplier<M, R> callBodyCriteria(Criteria<? super M> criteria) {
            ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(body(criteria)));
            return this;
        }

        public ChainedGetObjectFromArraySupplier<M, R> callBodyCriteria(String description, Predicate<? super M> predicate) {
            return callBodyCriteria(condition(description, predicate));
        }

        public ChainedGetObjectFromArraySupplier<M, R> callBodyCriteriaOr(Criteria<? super M>... criteria) {
            return callBodyCriteria(OR(criteria));
        }

        public ChainedGetObjectFromArraySupplier<M, R> callBodyCriteriaOnlyOne(Criteria<? super M>... criteria) {
            return callBodyCriteria(ONLY_ONE(criteria));
        }

        public ChainedGetObjectFromArraySupplier<M, R> callBodyCriteriaNot(Criteria<? super M>... criteria) {
            return callBodyCriteria(NOT(criteria));
        }
    }
}
