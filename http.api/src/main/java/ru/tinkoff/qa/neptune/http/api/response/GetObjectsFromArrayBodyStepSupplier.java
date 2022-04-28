package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;
import static ru.tinkoff.qa.neptune.http.api.response.dictionary.AdditionalCriteriaDescription.hasResultItem;

/**
 * Builds a step-function that retrieves an array from http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted array")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public final class GetObjectsFromArrayBodyStepSupplier<T, R>
    extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, GetObjectsFromArrayBodyStepSupplier<T, R>>
    implements DefinesResponseCriteria<T, GetObjectsFromArrayBodyStepSupplier<T, R>> {

    @Deprecated(forRemoval = true)
    private GetObjectsFromArrayBodyStepSupplier(Function<T, R[]> f) {
        super(httpResponse -> f.apply(httpResponse.body()));
        addIgnored(Exception.class);
    }

    private GetObjectsFromArrayBodyStepSupplier() {
        super(httpResponse -> ((Response<T, R[]>) httpResponse).getCalculated());
        addIgnored(Exception.class);
    }

    @Description("{description}")
    static <T, R> GetObjectsFromArrayBodyStepSupplier<T, R> asArray(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder<T> requestBuilder,
        Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromArrayBodyStepSupplier<T, R>()
            .from(response(requestBuilder, f).addIgnored(Exception.class));
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayBodyStepSupplier}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description is a description of resulted array
     * @param received    is a received http response
     * @param f           is a function that describes how to get an array from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of item of resulted array
     * @return an instance of {@link GetObjectsFromArrayBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R> GetObjectsFromArrayBodyStepSupplier<T, R> asArray(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        HttpResponse<T> received,
        Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromArrayBodyStepSupplier<>(f).from(received);
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayBodyStepSupplier}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description    is a description of resulted array
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an array from a body of http response
     * @param <T>            is a type of response body
     * @param <R>            is a type of item of resulted array
     * @return an instance of {@link GetObjectsFromArrayBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R> GetObjectsFromArrayBodyStepSupplier<T, R> asArray(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder<?> requestBuilder,
        HttpResponse.BodyHandler<T> handler,
        Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromArrayBodyStepSupplier<T, R>()
            .from(response(requestBuilder.responseBodyHandler(handler), f).addIgnored(Exception.class));
    }


    /**
     * Creates an instance of {@link GetObjectsFromArrayBodyStepSupplier}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description is a description of resulted array
     * @param received    is a received http response
     * @param <R>         is a type of item of array of response body
     * @return an instance of {@link GetObjectsFromArrayBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R> GetObjectsFromArrayBodyStepSupplier<R[], R> asArray(
        String description,
        HttpResponse<R[]> received) {
        return asArray(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayBodyStepSupplier}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description    is a description of resulted array
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of item of array of response body
     * @return an instance of {@link GetObjectsFromArrayBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R> GetObjectsFromArrayBodyStepSupplier<R[], R> asArray(
        String description,
        RequestBuilder<?> requestBuilder,
        HttpResponse.BodyHandler<R[]> handler) {
        return asArray(description, requestBuilder, handler, rs -> rs);
    }

    @Override
    public Function<HttpStepContext, R[]> get() {
        var fromVal = getFrom();
        Criteria<HttpResponse<T>> responseCriteria = null;
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            var resultCriteria = getCriteria();
            if (resultCriteria != null) {
                responseCriteria = condition(
                    hasResultItem(getDescription(), resultCriteria.toString()).toString(),
                    r -> stream(((Response<?, R[]>) r).getCalculated()).anyMatch(resultCriteria.get())
                );
            } else {
                responseCriteria = condition(
                    hasResultItem(getDescription()).toString(),
                    r -> ((Response<?, R[]>) r).getCalculated().length > 0
                );
            }
        }

        ofNullable(responseCriteria).ifPresent(this::responseCriteria);
        return super.get();
    }

    @Override
    public GetObjectsFromArrayBodyStepSupplier<T, R> throwOnNoResult() {
        var fromVal = getFrom();
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            ((ResponseSequentialGetSupplier<T>) fromVal).throwOnNoResult();
        }
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectsFromArrayBodyStepSupplier<T, R> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }
}
