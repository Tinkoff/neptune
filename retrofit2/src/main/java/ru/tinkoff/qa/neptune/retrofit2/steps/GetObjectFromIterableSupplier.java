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
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.iterableBodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.ResultCriteria.resultResponseCriteria;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectFromIterableSupplier<M, R> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<Iterable<R>>, GetObjectFromIterableSupplier<M, R>> {

    protected <S extends Iterable<R>> GetObjectFromIterableSupplier(Supplier<M> call, Function<M, S> f) {
        super(RequestExecutionResult::getResult);
        from((SendRequestAndGet<M, Iterable<R>>) getResponse(new GetStepResultFunction<>(f)).from(call));
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromIterableSupplier}
     */
    @Description("{description}")
    public static <M, R, S extends Iterable<R>> GetObjectFromIterableSupplier<M, R> iterableItem(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableSupplier<>(call, f);
    }

    /**
     * Creates a step that gets some value from iterable body of synchronous http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromIterableSupplier}
     */
    public static <R, S extends Iterable<R>> GetObjectFromIterableSupplier<S, R> iterableItem(String description,
                                                                                              Supplier<S> call) {
        return iterableItem(description, call, rs -> rs);
    }

    /**
     * Creates a step that gets some value from iterable which is calculated by body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param f           describes how to get desired value
     * @param <M>         deserialized body
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromIterableSupplier}
     */
    public static <M, R, S extends Iterable<R>> GetObjectFromIterableSupplier<M, R> callIterableItem(String description,
                                                                                                     Supplier<Call<M>> call,
                                                                                                     Function<M, S> f) {
        return iterableItem(description, new CallBodySupplier<>(call), f);
    }

    /**
     * Creates a step that gets some value from iterable body of http call.
     *
     * @param description is description of value to get
     * @param call        describes a single synchronous call
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromIterableSupplier}
     */
    public static <R, S extends Iterable<R>> GetObjectFromIterableSupplier<S, R> callIterableItem(String description,
                                                                                                  Supplier<Call<S>> call) {
        return callIterableItem(description, call, rs -> rs);
    }

    public GetObjectFromIterableSupplier<M, R> retryTimeOut(Duration timeOut) {
        ((SendRequestAndGet<M, Iterable<R>>) getFrom()).timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> pollingInterval(Duration timeOut) {
        ((SendRequestAndGet<M, Iterable<R>>) getFrom()).pollingInterval(timeOut);
        return this;
    }

    public GetObjectFromIterableSupplier<M, R> responseCriteria(Criteria<Response> criteria) {
        ((SendRequestAndGet<M, Iterable<R>>) getFrom()).criteria(resultResponseCriteria(criteria));
        return this;
    }

    public GetObjectFromIterableSupplier<M, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> criteria(Criteria<? super R> criteria) {
        ((SendRequestAndGet<M, Iterable<R>>) getFrom()).criteria(iterableBodyMatches(new BodyHasItems(criteria), criteria));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> throwOnNoResult() {
        ((SendRequestAndGet<M, Iterable<R>>) getFrom()).throwOnNoResult();
        return this;
    }
}
