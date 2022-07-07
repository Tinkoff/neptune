package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromArrayBodyStepSupplier.asOneOfArray;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asObject;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.body;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromIterableBodyStepSupplier.asOneOfIterable;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromArrayBodyStepSupplier.asArray;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromIterableBodyStepSupplier.asIterable;

public final class GetResponseDataStepFactory {

    private GetResponseDataStepFactory() {
        super();
    }

    public static <M, T> GetObjectFromBodyStepSupplier<M, T> getObjectFromResponseBody(
        String description,
        RequestBuilder<M> requestBuilder,
        Function<M, T> f
    ) {
        return asObject(description, requestBuilder, f);
    }

    public static <T> GetObjectFromBodyStepSupplier<T, T> getBody(
        RequestBuilder<T> requestBuilder
    ) {
        return body(requestBuilder);
    }

    public static <M, T> GetObjectsFromArrayBodyStepSupplier<M, T> getArrayFromResponseBody(
        String description,
        RequestBuilder<M> requestBuilder,
        Function<M, T[]> f
    ) {
        return asArray(description, requestBuilder, f);
    }

    public static <M, T, S extends Iterable<T>> GetObjectsFromIterableBodyStepSupplier<M, T, S> getListFromResponseBody(
        String description,
        RequestBuilder<M> requestBuilder,
        Function<M, S> f
    ) {
        return asIterable(description, requestBuilder, f);
    }

    public static <M, T> GetObjectFromArrayBodyStepSupplier<M, T> getArrayItemFromResponseBody(
        String description,
        RequestBuilder<M> requestBuilder,
        Function<M, T[]> f
    ) {
        return asOneOfArray(description, requestBuilder, f);
    }

    public static <M, T, S extends Iterable<T>> GetObjectFromIterableBodyStepSupplier<M, T> getItemFromResponseBody(
        String description,
        RequestBuilder<M> requestBuilder,
        Function<M, S> f
    ) {
        return asOneOfIterable(description, requestBuilder, f);
    }
}
