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
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;
import static ru.tinkoff.qa.neptune.http.api.response.dictionary.AdditionalCriteriaDescription.isPossibleToGetExpectedValue;

/**
 * Builds a step-function that retrieves an object from http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public final class GetObjectFromBodyStepSupplier<T, R>
    extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, GetObjectFromBodyStepSupplier<T, R>>
    implements DefinesResponseCriteria<T, GetObjectFromBodyStepSupplier<T, R>> {

    @Deprecated(forRemoval = true)
    private GetObjectFromBodyStepSupplier(Function<T, R> f) {
        super(httpResponse -> f.apply(httpResponse.body()));
        addIgnored(Exception.class);
    }

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

    /**
     * Creates an instance of {@link GetObjectFromBodyStepSupplier}. It builds a step-function that retrieves
     * an object from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get resulted object from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of resulted object
     * @return an instance of {@link GetObjectFromBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R> GetObjectFromBodyStepSupplier<T, R> asObject(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
        HttpResponse<T> received,
        Function<T, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromBodyStepSupplier<>(f).from(received);
    }

    /**
     * Creates an instance of {@link GetObjectFromBodyStepSupplier}. It builds a step-function that retrieves
     * a body of http response.
     *
     * @param received is a received http response
     * @param <T>      is a type of response body
     * @return an instance of {@link GetObjectFromBodyStepSupplier}
     */
    @Description("Body of http response")
    @Deprecated(forRemoval = true)
    public static <T> GetObjectFromBodyStepSupplier<T, T> asIs(HttpResponse<T> received) {
        return new GetObjectFromBodyStepSupplier<T, T>(t -> t).from(received);
    }

    /**
     * Creates an instance of {@link GetObjectFromBodyStepSupplier}. It builds a step-function that retrieves
     * an object from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get resulted object from a body of http response
     * @param <T>            is a type of response body
     * @param <R>            is a type of resulted object
     * @return an instance of {@link GetObjectFromBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R> GetObjectFromBodyStepSupplier<T, R> asObject(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder<?> requestBuilder,
        HttpResponse.BodyHandler<T> handler,
        Function<T, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromBodyStepSupplier<T, R>()
            .from(response(requestBuilder.responseBodyHandler(handler), f).addIgnored(Exception.class));
    }

    /**
     * Creates an instance of {@link GetObjectFromBodyStepSupplier}. It builds a step-function that retrieves
     * a body of http response.
     *
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <T>            is a type of response body
     * @return an instance of {@link GetObjectFromBodyStepSupplier}
     */
    @Description("Body of http response")
    @Deprecated(forRemoval = true)
    public static <T> GetObjectFromBodyStepSupplier<T, T> asIs(RequestBuilder<?> requestBuilder,
                                                               HttpResponse.BodyHandler<T> handler) {
        return new GetObjectFromBodyStepSupplier<T, T>()
            .from(response(requestBuilder.responseBodyHandler(handler), t -> t).addIgnored(Exception.class));
    }

    @Override
    public GetObjectFromBodyStepSupplier<T, R> throwOnNoResult() {
        var fromVal = getFrom();
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            ((ResponseSequentialGetSupplier<T>) fromVal).throwOnNoResult();
        }
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectFromBodyStepSupplier<T, R> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }

    @Override
    public Function<HttpStepContext, R> get() {
        var fromVal = getFrom();
        Criteria<HttpResponse<T>> responseCriteria = null;
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            var resultCriteria = getCriteria();
            if (resultCriteria != null) {
                responseCriteria = condition(
                    isPossibleToGetExpectedValue(getDescription(), resultCriteria.toString()).toString(),
                    r -> resultCriteria.get().test(((Response<?, R>) r).getCalculated())
                );
            }
        }

        ofNullable(responseCriteria).ifPresent(this::responseCriteria);
        return super.get();
    }
}
