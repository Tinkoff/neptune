package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import retrofit2.Call;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;
import ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria;

import java.time.Duration;
import java.util.Objects;
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
public abstract class GetObjectSupplier<M, R, S extends GetObjectSupplier<M, R, S>> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<M, R>, S>
        implements DefinesResponseCriteria<S> {

    protected GetObjectSupplier(SendRequestAndGet<M, R> from) {
        super(RequestExecutionResult::getResult);
        from(from);
    }

    /**
     * Creates a step that gets some value from object which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of object
     * @return an instance of {@link ChainedGetObjectSupplier}
     */
    @Description("{description}")
    public static <M, R> ChainedGetObjectSupplier<M, R> object(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new ChainedGetObjectSupplier<>(getResponse(translate(description),
                new GetStepResultFunction<>(f, Objects::nonNull))
                .from(call));
    }

    /**
     * Creates a step that gets body of synchronous http call.
     *
     * @param call describes a single synchronous call
     * @param <M>  deserialized body
     * @return an instance of {@link SimpleGetObjectSupplier}
     */
    @Description("Response body")
    public static <M> SimpleGetObjectSupplier<M> body(Supplier<M> call) {
        return new SimpleGetObjectSupplier<>(getResponse(new GetStepResultFunction<M, M>(m -> m, null)).from(call));
    }

    /**
     * Creates a step that gets some value from object which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of object
     * @return an instance of {@link ChainedGetObjectSupplier}
     */
    public static <M, R> ChainedGetObjectSupplier<M, R> callObject(String description,
                                                                   Supplier<Call<M>> call,
                                                                   Function<M, R> f) {
        return object(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets body of http call.
     *
     * @param call describes a single synchronous call
     * @param <M>  deserialized body
     * @return an instance of {@link SimpleGetObjectSupplier}
     */
    public static <M> SimpleGetObjectSupplier<M> callBody(Supplier<Call<M>> call) {
        return body(new CallBodySupplier<>(call));
    }

    @Override
    public S retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, R>) getFrom()).timeOut(timeOut);
        return (S) this;
    }

    @Override
    public S pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, R>) getFrom()).pollingInterval(timeOut);
        return (S) this;
    }

    /**
     * Defines criteria for expected response
     *
     * @param criteria criteria for expected response
     * @return self-reference
     * @see ResponseCriteria
     * @see Criteria#condition(String, Predicate)
     */
    @Override
    public S responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(criteria));
        return (S) this;
    }


    @Override
    public S responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }


    @Override
    @SafeVarargs
    public final S responseCriteriaOr(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(OR(criteria)));
        return (S) this;
    }

    @Override
    @SafeVarargs
    public final S responseCriteriaOnlyOne(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(ONLY_ONE(criteria)));
        return (S) this;
    }

    @Override
    @SafeVarargs
    public final S responseCriteriaNot(Criteria<Response>... criteria) {
        ((SendRequestAndGet<M, R>) getFrom()).criteria(resultResponseCriteria(NOT(criteria)));
        return (S) this;
    }

    @Override
    public S throwOnNoResult() {
        ((SendRequestAndGet<M, R>) getFrom()).throwOnNoResult();
        super.throwOnNoResult();
        return (S) this;
    }

    public static class SimpleGetObjectSupplier<M> extends GetObjectSupplier<M, M, SimpleGetObjectSupplier<M>> {

        private SimpleGetObjectSupplier(SendRequestAndGet<M, M> from) {
            super(from);
        }
    }

    public static class ChainedGetObjectSupplier<M, R> extends GetObjectSupplier<M, R, ChainedGetObjectSupplier<M, R>> {

        private ChainedGetObjectSupplier(SendRequestAndGet<M, R> from) {
            super(from);
        }

        public ChainedGetObjectSupplier<M, R> callBodyCriteria(Criteria<? super M> criteria) {
            ((SendRequestAndGet<M, R>) getFrom()).criteria(bodyMatches(BodyMatches.body(criteria)));
            return this;
        }

        public ChainedGetObjectSupplier<M, R> callBodyCriteria(String description, Predicate<? super M> predicate) {
            return callBodyCriteria(condition(description, predicate));
        }

        public ChainedGetObjectSupplier<M, R> callBodyCriteriaOr(Criteria<? super M>... criteria) {
            return callBodyCriteria(OR(criteria));
        }

        public ChainedGetObjectSupplier<M, R> callBodyCriteriaOnlyOne(Criteria<? super M>... criteria) {
            return callBodyCriteria(ONLY_ONE(criteria));
        }

        public ChainedGetObjectSupplier<M, R> callBodyCriteriaNot(Criteria<? super M>... criteria) {
            return callBodyCriteria(NOT(criteria));
        }
    }
}
