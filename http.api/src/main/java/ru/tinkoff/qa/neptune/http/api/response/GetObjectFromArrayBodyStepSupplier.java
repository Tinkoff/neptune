package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
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
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

/**
 * It builds a step-function that retrieves an object from array which is retrieved from
 * http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
public final class GetObjectFromArrayBodyStepSupplier<T, R>
    extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, GetObjectFromArrayBodyStepSupplier<T, R>>
    implements DefinesResponseCriteria<T, GetObjectFromArrayBodyStepSupplier<T, R>> {

    private GetObjectFromArrayBodyStepSupplier() {
        super(tHttpResponse -> ((Response<T, R[]>) tHttpResponse).getCalculated());
        addIgnored(Exception.class);
    }

    @Description("{description}")
    static <T, R> GetObjectFromArrayBodyStepSupplier<T, R> asOneOfArray(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder<T> requestBuilder,
        Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromArrayBodyStepSupplier<T, R>()
            .from(response(requestBuilder, f).addIgnored(Exception.class));
    }

    @Override
    public Function<HttpStepContext, R> get() {
        var fromVal = getFrom();
        Criteria<HttpResponse<T>> responseCriteria = DefinesResponseCriteria.getResponseCriteriaForIterables(
            fromVal,
            getCriteria(),
            getDescription());

        ofNullable(responseCriteria).ifPresent(this::responseCriteria);
        return super.get();
    }

    @Override
    public GetObjectFromArrayBodyStepSupplier<T, R> throwOnNoResult() {
        DefinesResponseCriteria.super.throwOnNoResult();
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectFromArrayBodyStepSupplier<T, R> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }
}