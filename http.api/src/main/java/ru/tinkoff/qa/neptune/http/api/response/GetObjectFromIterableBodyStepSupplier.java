package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

/**
 * Builds a step-function that retrieves an object from some {@link Iterable} which is retrieved from
 * http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
public final class GetObjectFromIterableBodyStepSupplier<T, R>
    extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, GetObjectFromIterableBodyStepSupplier<T, R>>
    implements DefinesResponseCriteria<T, GetObjectFromIterableBodyStepSupplier<T, R>> {


    private <Q extends Iterable<R>> GetObjectFromIterableBodyStepSupplier() {
        super(httpResponse -> ((Response<T, Q>) httpResponse).getCalculated());
        addIgnored(Exception.class);
    }

    @Description("{description}")
    static <T, R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<T, R> asOneOfIterable(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
        RequestBuilder<T> requestBuilder,
        Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableBodyStepSupplier<T, R>()
            .from(response(requestBuilder, f).addIgnored(Exception.class));
    }

    @Override
    public GetObjectFromIterableBodyStepSupplier<T, R> throwOnNoResult() {
        DefinesResponseCriteria.super.throwOnNoResult();
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectFromIterableBodyStepSupplier<T, R> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }
}