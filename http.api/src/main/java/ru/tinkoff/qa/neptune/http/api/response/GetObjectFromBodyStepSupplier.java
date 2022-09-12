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
 * Builds a step-function that retrieves an object from http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
public final class GetObjectFromBodyStepSupplier<T, R>
    extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, GetObjectFromBodyStepSupplier<T, R>>
    implements DefinesResponseCriteria<T, GetObjectFromBodyStepSupplier<T, R>> {

    private GetObjectFromBodyStepSupplier() {
        super(httpResponse -> ((Response<T, R>) httpResponse).getCalculated());
        addIgnored(Exception.class);
    }

    @Description("{description}")
    static <T, R> GetObjectFromBodyStepSupplier<T, R> asObject(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder<T> requestBuilder,
        Function<T, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromBodyStepSupplier<T, R>()
            .from(response(requestBuilder, f).addIgnored(Exception.class));
    }

    @Description("Body of http response")
    static <T> GetObjectFromBodyStepSupplier<T, T> body(
        RequestBuilder<T> requestBuilder) {
        return new GetObjectFromBodyStepSupplier<T, T>()
            .from(response(requestBuilder, t -> t).addIgnored(Exception.class));
    }

    @Override
    public GetObjectFromBodyStepSupplier<T, R> throwOnNoResult() {
        DefinesResponseCriteria.super.throwOnNoResult();
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectFromBodyStepSupplier<T, R> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }
}