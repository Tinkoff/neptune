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

@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectFromIterableSupplier<M, R> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<RetrofitContext, R, RequestExecutionResult<Iterable<R>>, GetObjectFromIterableSupplier<M, R>> {

    private final SendRequestAndGet<M, Iterable<R>> delegateTo;

    @SuppressWarnings("unchecked")
    protected <S extends Iterable<R>> GetObjectFromIterableSupplier(Supplier<M> call, Function<M, S> f) {
        super(RequestExecutionResult::getResult);
        this.delegateTo = (SendRequestAndGet<M, Iterable<R>>) getResponse(new GetStepResultFunction<>(f)).from(call);
        from(this.delegateTo);
    }

    @Description("{description}")
    public static <M, R, S extends Iterable<R>> GetObjectFromIterableSupplier<M, R> iterableItem(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Supplier<M> call, Function<M, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableSupplier<>(call, f);
    }

    public static <R, S extends Iterable<R>> GetObjectFromIterableSupplier<S, R> iterableItem(String description, Supplier<S> call) {
        return iterableItem(description, call, rs -> rs);
    }

    public static <M, R, S extends Iterable<R>> GetObjectFromIterableSupplier<M, R> callIterableItem(String description,
                                                                                                     Supplier<Call<M>> call,
                                                                                                     Function<M, S> f) {
        return iterableItem(description, new CallBodySupplier<>(call), f);
    }

    public static <R, S extends Iterable<R>> GetObjectFromIterableSupplier<S, R> callIterableItem(String description,
                                                                                                  Supplier<Call<S>> call) {
        return callIterableItem(description, call, rs -> rs);
    }

    public GetObjectFromIterableSupplier<M, R> retryTimeOut(Duration timeOut) {
        delegateTo.timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> pollingInterval(Duration timeOut) {
        delegateTo.pollingInterval(timeOut);
        return this;
    }

    public GetObjectFromIterableSupplier<M, R> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetObjectFromIterableSupplier<M, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(new BodyHasItems(criteria.toString()).toString(),
                r -> stream(r.spliterator(), false).anyMatch(criteria.get())));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectFromIterableSupplier<M, R> throwOnNoResult() {
        delegateTo.throwOnNoResult();
        return this;
    }
}
