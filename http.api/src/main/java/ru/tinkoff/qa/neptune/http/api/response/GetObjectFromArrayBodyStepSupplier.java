package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.response.dictionary.HasAtLeastOneItem;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;
import static ru.tinkoff.qa.neptune.http.api.response.dictionary.CalculatedResponseDateHasItem.hasResultItem;

/**
 * It builds a step-function that retrieves an object from array which is retrieved from
 * http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public final class GetObjectFromArrayBodyStepSupplier<T, R>
    extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, GetObjectFromArrayBodyStepSupplier<T, R>>
    implements DefinesResponseCriteria<T, GetObjectFromArrayBodyStepSupplier<T, R>> {

    private final Function<T, R[]> calculation;

    private GetObjectFromArrayBodyStepSupplier(Function<T, R[]> f) {
        super(httpResponse -> f.apply(httpResponse.body()));
        addIgnored(Exception.class);
        calculation = f;
    }

    /**
     * Creates an instance of {@link GetObjectFromArrayBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get an array from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of resulted object
     * @return an instance of {@link GetObjectFromArrayBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R> GetObjectFromArrayBodyStepSupplier<T, R> asOneOfArray(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        HttpResponse<T> received,
        Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        checkNotNull(received);
        return new GetObjectFromArrayBodyStepSupplier<>(f).from(received);
    }

    /**
     * Creates an instance of {@link GetObjectFromArrayBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an array from a body of http response
     * @param <T>            is a type of response body
     * @param <R>            is a type of resulted object
     * @return an instance of {@link GetObjectFromArrayBodyStepSupplier}
     */
    @Description("{description}")
    public static <T, R> GetObjectFromArrayBodyStepSupplier<T, R> asOneOfArray(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder requestBuilder,
        HttpResponse.BodyHandler<T> handler,
        Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromArrayBodyStepSupplier<>(f)
            .from(response(requestBuilder, handler).addIgnored(Exception.class));
    }


    /**
     * Creates an instance of {@link GetObjectFromArrayBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param <R>         is a type of an item of array of response body
     * @return an instance of {@link GetObjectFromArrayBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R> GetObjectFromArrayBodyStepSupplier<R[], R> asOneOfArray(
        String description,
        HttpResponse<R[]> received) {
        return asOneOfArray(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectFromArrayBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of an item of array of response body
     * @return an instance of {@link GetObjectFromArrayBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R> GetObjectFromArrayBodyStepSupplier<R[], R> asOneOfArray(
        String description,
        RequestBuilder requestBuilder,
        HttpResponse.BodyHandler<R[]> handler) {
        return asOneOfArray(description, requestBuilder, handler, rs -> rs);
    }

    @Override
    public Function<HttpStepContext, R> get() {
        var fromVal = getFrom();
        Criteria<HttpResponse<T>> responseCriteria = null;
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            var resultCriteria = getCriteria();
            if (resultCriteria != null) {
                responseCriteria = condition(
                    hasResultItem(getDescription(), resultCriteria.toString()).toString(),
                    r -> stream(calculation.apply(r.body())).anyMatch(resultCriteria.get())
                );
            } else {
                responseCriteria = condition(
                    hasResultItem(getDescription(), new HasAtLeastOneItem().toString()).toString(),
                    r -> calculation.apply(r.body()).length > 0
                );
            }
        }

        ofNullable(responseCriteria).ifPresent(this::responseCriteria);
        return super.get();
    }

    @Override
    public GetObjectFromArrayBodyStepSupplier<T, R> throwOnNoResult() {
        var fromVal = getFrom();
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            ((ResponseSequentialGetSupplier<T>) fromVal).throwOnNoResult();
        }
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectFromArrayBodyStepSupplier<T, R> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }

}