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
import java.util.List;
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
 * Builds a step-function that retrieves an {@link Iterable} from http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public final class GetObjectsFromIterableBodyStepSupplier<T, R, S extends Iterable<R>>
    extends SequentialGetStepSupplier.GetListChainedStepSupplier<HttpStepContext, S, HttpResponse<T>, R, GetObjectsFromIterableBodyStepSupplier<T, R, S>>
    implements DefinesResponseCriteria<T, GetObjectsFromIterableBodyStepSupplier<T, R, S>> {

    @Deprecated(forRemoval = true)
    private GetObjectsFromIterableBodyStepSupplier(Function<T, S> f) {
        super(httpResponse -> f.apply(httpResponse.body()));
        addIgnored(Exception.class);
    }

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

    /**
     * Creates an instance of {@link GetObjectsFromIterableBodyStepSupplier}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description is a description of resulted {@link Iterable}
     * @param received    is a received http response
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of item of resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetObjectsFromIterableBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<T, R, S> asIterable(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        HttpResponse<T> received,
        Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromIterableBodyStepSupplier<>(f).from(received);
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableBodyStepSupplier}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description    is a description of resulted {@link Iterable}
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>            is a type of response body
     * @param <R>            is a type of item of resulted iterable
     * @param <S>            is a type of resulted iterable
     * @return an instance of {@link GetObjectsFromIterableBodyStepSupplier}
     */
    @Description("{description}")
    @Deprecated(forRemoval = true)
    public static <T, R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<T, R, S> asIterable(
        @DescriptionFragment(
            value = "description",
            makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
            String description,
        RequestBuilder<?> requestBuilder,
        HttpResponse.BodyHandler<T> handler,
        Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromIterableBodyStepSupplier<T, R, S>()
            .from(response(requestBuilder.responseBodyHandler(handler), f).addIgnored(Exception.class));
    }


    /**
     * Creates an instance of {@link GetObjectsFromIterableBodyStepSupplier}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description is a description of resulted {@link Iterable}
     * @param received    is a received http response
     * @param <R>         is a type of element of an iterable of response body
     * @param <S>         if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectsFromIterableBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<S, R, S> asIterable(
        String description,
        HttpResponse<S> received) {
        return asIterable(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableBodyStepSupplier}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description    is a description of resulted {@link Iterable}
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of element of an iterable of response body
     * @param <S>            if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectsFromIterableBodyStepSupplier}
     */
    @Deprecated(forRemoval = true)
    public static <R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<S, R, S> asIterable(
        String description,
        RequestBuilder<?> requestBuilder,
        HttpResponse.BodyHandler<S> handler) {
        return asIterable(description, requestBuilder, handler, rs -> rs);
    }

    @Override
    public GetObjectsFromIterableBodyStepSupplier<T, R, S> throwOnNoResult() {
        var fromVal = getFrom();
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            ((ResponseSequentialGetSupplier<T>) fromVal).throwOnNoResult();
        }
        return super.throwOnNoResult();
    }

    @Override
    public GetObjectsFromIterableBodyStepSupplier<T, R, S> pollingInterval(Duration timeOut) {
        return DefinesResponseCriteria.super.pollingInterval(timeOut);
    }

    @Override
    public Function<HttpStepContext, List<R>> get() {
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

