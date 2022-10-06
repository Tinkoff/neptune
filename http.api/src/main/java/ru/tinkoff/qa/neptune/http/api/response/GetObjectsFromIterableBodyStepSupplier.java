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
 * Builds a step-function that retrieves an {@link Iterable} from http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public final class GetObjectsFromIterableBodyStepSupplier<T, R, S extends Iterable<R>>
    extends SequentialGetStepSupplier.GetListChainedStepSupplier<HttpStepContext, S, HttpResponse<T>, R, GetObjectsFromIterableBodyStepSupplier<T, R, S>>
    implements DefinesResponseCriteria<T, GetObjectsFromIterableBodyStepSupplier<T, R, S>> {

    private GetObjectsFromIterableBodyStepSupplier() {
        super(httpResponse -> ((Response<T, S>) httpResponse).getCalculated());
        addIgnored(Exception.class);
    }

    @Description("{description}")
    static <T, R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<T, R, S> asIterable(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder<T> requestBuilder,
        Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromIterableBodyStepSupplier<T, R, S>()
            .from(response(requestBuilder, f).addIgnored(Exception.class));
    }

    @Override
    public GetObjectsFromIterableBodyStepSupplier<T, R, S> throwOnNoResult() {
        DefinesResponseCriteria.super.throwOnNoResult();
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectsFromIterableBodyStepSupplier<T, R, S> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }
}