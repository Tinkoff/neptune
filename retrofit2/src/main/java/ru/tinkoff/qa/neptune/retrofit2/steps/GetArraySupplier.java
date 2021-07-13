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
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;

@Description("{description}")
@ThrowWhenNoData(startDescription = "Not received", toThrow = DataHasNotBeenReceivedException.class)
public class GetArraySupplier<T, R> extends SequentialGetStepSupplier
        .GetArrayChainedStepSupplier<RetrofitContext<T>, R, RequestExecutionResult<R[]>, GetArraySupplier<T, R>> {

    private final SendRequestAndGet<T, R[]> delegateTo;

    protected GetArraySupplier(Function<T, R[]> f) {
        super(RequestExecutionResult::getResult);
        this.delegateTo = new SendRequestAndGet<>(new GetStepResultFunction<>(f));
        from(this.delegateTo);
    }

    @Description("{description}")
    public static <T, R> GetArraySupplier<T, R> arrayFromCall(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, Call<R[]>> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetArraySupplier<>(f.andThen(call -> {
            try {
                return call.execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Description("{description}")
    public static <T, R> GetArraySupplier<T, R> array(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetArraySupplier<>(f);
    }

    public GetArraySupplier<T, R> retryTimeOut(Duration timeOut) {
        delegateTo.timeOut(timeOut);
        return this;
    }

    public GetArraySupplier<T, R> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetArraySupplier<T, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    protected GetArraySupplier<T, R> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(new BodyHasItems(criteria.toString()).toString(),
                r -> stream(r).anyMatch(criteria.get())));
        return super.criteria(criteria);
    }

    @Override
    public GetArraySupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return criteria(condition(description, predicate));
    }
}
