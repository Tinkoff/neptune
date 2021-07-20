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
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public class GetIterableSupplier<M, R, S extends Iterable<R>> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<RetrofitContext, S, RequestExecutionResult<S>, R, GetIterableSupplier<M, R, S>> {

    protected GetIterableSupplier(Supplier<M> call, Function<M, S> f) {
        super(RequestExecutionResult::getResult);
        from(getResponse(new GetStepResultFunction<>(f)).from(call));
    }

    @Description("{description}")
    public static <M, R, S extends Iterable<R>> GetIterableSupplier<M, R, S> iterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetIterableSupplier<>(call, f);
    }

    public static <R, S extends Iterable<R>> GetIterableSupplier<S, R, S> iterable(String description,
                                                                                   Supplier<S> call) {
        return iterable(description, call, rs -> rs);
    }

    public static <M, R, S extends Iterable<R>> GetIterableSupplier<M, R, S> callIterable(String description,
                                                                                          Supplier<Call<M>> call, Function<M, S> f) {
        return iterable(description, new CallBodySupplier<>(call), f);
    }

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
        ((SendRequestAndGet<M, S>) getFrom()).criteria(condition(criteria.toString(), r -> criteria.get()
                .test(r.getLastResponse())));
        return this;
    }

    public GetIterableSupplier<M, R, S> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetIterableSupplier<M, R, S> criteria(Criteria<? super R> criteria) {
        ((SendRequestAndGet<M, S>) getFrom()).criteria(bodyMatches(new BodyHasItems(criteria.toString()).toString(),
                r -> stream(r.spliterator(), false).anyMatch(criteria.get())));
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
