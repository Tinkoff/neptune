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
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.retrofit2.steps.SendRequestAndGet.getResponse;

@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public class GetObjectSupplier<T, R> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<RetrofitContext<T>, R, RequestExecutionResult<R>, GetObjectSupplier<T, R>> {

    private final SendRequestAndGet<T, R> delegateTo;

    protected GetObjectSupplier(Function<T, R> f) {
        super(RequestExecutionResult::getResult);
        this.delegateTo = getResponse(new GetStepResultFunction<>(f));
        from(this.delegateTo);
    }

    @Description("{description}")
    public static <T, R> GetObjectSupplier<T, R> bodyFromCall(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, Call<R>> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectSupplier<>(f.andThen(call -> {
            try {
                return call.execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Description("Response body")
    public static <T, R> GetObjectSupplier<T, R> bodyFromCall(Function<T, Call<R>> f) {
        return new GetObjectSupplier<>(f.andThen(call -> {
            try {
                return call.execute().body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Description("{description}")
    public static <T, R> GetObjectSupplier<T, R> body(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectSupplier<>(f);
    }

    @Description("Response body")
    public static <T, R> GetObjectSupplier<T, R> body(Function<T, R> f) {
        return new GetObjectSupplier<>(f);
    }

    public GetObjectSupplier<T, R> retryTimeOut(Duration timeOut) {
        delegateTo.timeOut(timeOut);
        return this;
    }

    @Override
    public GetObjectSupplier<T, R> pollingInterval(Duration timeOut) {
        delegateTo.pollingInterval(timeOut);
        return this;
    }

    public GetObjectSupplier<T, R> responseCriteria(Criteria<Response> criteria) {
        delegateTo.criteria(condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse())));
        return this;
    }

    public GetObjectSupplier<T, R> responseCriteria(String description, Predicate<Response> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    @Override
    public GetObjectSupplier<T, R> criteria(Criteria<? super R> criteria) {
        delegateTo.criteria(bodyMatches(criteria.toString(), r -> criteria.get().test(r)));
        return super.criteria(criteria);
    }

    @Override
    public GetObjectSupplier<T, R> criteria(String description, Predicate<? super R> criteria) {
        return criteria(condition(translate(description), criteria));
    }

    @Override
    public GetObjectSupplier<T, R> throwOnNoResult() {
        delegateTo.throwOnNoResult();
        return this;
    }
}
