package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import retrofit2.Call;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;

@Description("{description}")
@ThrowWhenNoData(startDescription = "Not received", toThrow = DataHasNotBeenReceivedException.class)
public class GetIterableSupplier<T, R, S extends Iterable<R>> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<RetrofitContext<T>, S, RequestExecutionResult<S>, R, GetIterableSupplier<T, R, S>> {

    private final SendRequestAndGet<T, S> delegateTo;

    protected GetIterableSupplier(Function<T, S> f) {
        super(RequestExecutionResult::getResult);
        delegateTo = new SendRequestAndGet<>(new GetStepResultFunction<>(f));
        from(this.delegateTo);
    }

    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetIterableSupplier<T, R, S> iterableFromCall(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, Call<S>> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetIterableSupplier<>(f.andThen(call -> {
            try {
                return call.execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetIterableSupplier<T, R, S> iterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetIterableSupplier<>(f);
    }

    public GetIterableSupplier<T, R, S> retryTimeOut(Duration timeOut) {
        delegateTo.timeOut(timeOut);
        return this;
    }

    public GetIterableSupplier<T, R, S> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetIterableSupplier<T, R, S> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    protected GetIterableSupplier<T, R, S> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(new BodyHasItems(criteria.toString()).toString(),
                r -> stream(r.spliterator(), false).anyMatch(criteria.get())));
        return super.criteria(criteria);
    }

    @Override
    public GetIterableSupplier<T, R, S> criteria(String description, Predicate<? super R> predicate) {
        return criteria(condition(description, predicate));
    }

    @Override
    public GetIterableSupplier<T, R, S> throwOnNoResult() {
        delegateTo.throwOnNoResult();
        return super.throwOnNoResult();
    }
}
