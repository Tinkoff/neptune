package ru.tinkoff.qa.neptune.http.api.response;


import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class GetObjectFromIterableBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<HttpResponse<T>, R, GetObjectFromIterableBodyStepSupplier<T, R>> {

    private <S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier(String description, Function<T, S> f) {
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
    public GetObjectFromIterableBodyStepSupplier<T, R> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    public GetObjectFromIterableBodyStepSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected GetObjectFromIterableBodyStepSupplier<T, R> throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }
}
