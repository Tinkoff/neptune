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
import static com.google.common.collect.Streams.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;
import static ru.tinkoff.qa.neptune.http.api.response.dictionary.AdditionalCriteriaDescription.hasResultItem;

/**
 * Builds a step-function that retrieves an object from some {@link Iterable} which is retrieved from
 * http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public final class GetObjectFromIterableBodyStepSupplier<T, R>
    extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, GetObjectFromIterableBodyStepSupplier<T, R>>
    implements DefinesResponseCriteria<T, GetObjectFromIterableBodyStepSupplier<T, R>> {

    @Deprecated(forRemoval = true)
    private <Q extends Iterable<R>> GetObjectFromIterableBodyStepSupplier(Function<T, Q> f) {
        super(httpResponse -> f.apply(httpResponse.body()));
        addIgnored(Exception.class);
    }

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

    /**
     * Creates an instance of {@link GetObjectFromIterableBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of resulted object
     * @param <S>         if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<T, R> asOneOfIterable(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        HttpResponse<T> received,
        Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableBodyStepSupplier<>(f).from(received);
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>            is a type of response body
     * @param <R>            is a type of resulted object
     * @param <S>            if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<T, R> asOneOfIterable(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
        RequestBuilder<?> requestBuilder,
        HttpResponse.BodyHandler<T> handler,
        Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableBodyStepSupplier<T, R>()
            .from(response(requestBuilder.responseBodyHandler(handler), f).addIgnored(Exception.class));
    }


    /**
     * Creates an instance of {@link GetObjectFromIterableBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param <R>         is a type of element of an iterable of response body
     * @param <S>         if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectFromIterableBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<S, R> asOneOfIterable(
        String description,
        HttpResponse<S> received) {
        return asOneOfIterable(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of element of an iterable of response body
     * @param <S>            if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectFromIterableBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<S, R> asOneOfIterable(
        String description,
        RequestBuilder<?> requestBuilder,
        HttpResponse.BodyHandler<S> handler) {
        return asOneOfIterable(description, requestBuilder, handler, rs -> rs);
    }

    @Override
    public GetObjectFromIterableBodyStepSupplier<T, R> throwOnNoResult() {
        var fromVal = getFrom();
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            ((ResponseSequentialGetSupplier<T>) fromVal).throwOnNoResult();
        }
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectFromIterableBodyStepSupplier<T, R> pollingInterval(Duration timeOut) {
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
                    hasResultItem(getDescription(), resultCriteria.toString()).toString(),
                    r -> stream(((Response<?, Iterable<R>>) r).getCalculated()).anyMatch(resultCriteria.get())
                );
            } else {
                responseCriteria = condition(
                    hasResultItem(getDescription()).toString(),
                    r -> !isEmpty(((Response<?, Iterable<R>>) r).getCalculated())
                );
            }
        }

        ofNullable(responseCriteria).ifPresent(this::responseCriteria);
        return super.get();
    }
}
