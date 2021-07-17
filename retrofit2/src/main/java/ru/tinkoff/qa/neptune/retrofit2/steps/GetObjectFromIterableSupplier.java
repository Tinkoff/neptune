package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import retrofit2.Call;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.retrofit2.RetrofitContext;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectFromIterableSupplier<T, R> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<RetrofitContext<T>, R, RequestExecutionResult<Iterable<R>>, GetObjectFromIterableSupplier<T, R>> {

    private final SendRequestAndGet<T, Iterable<R>> delegateTo;

    @SuppressWarnings("unchecked")
    protected <S extends Iterable<R>> GetObjectFromIterableSupplier(Function<T, S> f) {
        super(RequestExecutionResult::getResult);
        this.delegateTo = (SendRequestAndGet<T, Iterable<R>>) getResponse(new GetStepResultFunction<>(f));
        from(this.delegateTo);
    }

    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableSupplier<T, R> iterableItemFromCall(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, Call<S>> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableSupplier<>(f.andThen(call -> {
            try {
                return call.execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableSupplier<T, R> iterableItem(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableSupplier<>(f);
    }

    public GetObjectFromIterableSupplier<T, R> retryTimeOut(Duration timeOut) {
        delegateTo.timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectFromIterableSupplier<T, R> pollingInterval(Duration timeOut) {
        delegateTo.pollingInterval(timeOut);
        return this;
    }

    public GetObjectFromIterableSupplier<T, R> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetObjectFromIterableSupplier<T, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetObjectFromIterableSupplier<T, R> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(new BodyHasItems(criteria.toString()).toString(),
                r -> stream(r.spliterator(), false).anyMatch(criteria.get())));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectFromIterableSupplier<T, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectFromIterableSupplier<T, R> throwOnNoResult() {
        delegateTo.throwOnNoResult();
        return this;
    }
}
