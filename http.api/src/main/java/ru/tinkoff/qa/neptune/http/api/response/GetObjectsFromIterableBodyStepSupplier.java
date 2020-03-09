package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class GetObjectsFromIterableBodyStepSupplier<T, R, S extends Iterable<R>>
        extends SequentialGetStepSupplier.GetIterableStepSupplier<HttpResponse<T>, S, R, GetObjectsFromIterableBodyStepSupplier<T, R, S>> {

    private GetObjectsFromIterableBodyStepSupplier(String description, Function<T, S> f) {
        super(description, ((Function<HttpResponse<T>, T>) HttpResponse::body).andThen(t -> {
            try {
                return f.apply(t);
            }
            catch (Throwable thrown) {
                thrown.printStackTrace();
                return null;
            }
        }));
    }

    @Override
    public GetObjectsFromIterableBodyStepSupplier<T, R, S> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public GetObjectsFromIterableBodyStepSupplier<T, R, S> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    protected GetObjectsFromIterableBodyStepSupplier<T, R, S> throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }
}
