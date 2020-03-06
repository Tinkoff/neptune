package ru.tinkoff.qa.neptune.http.api.response.support;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class GetObjectFromArrayBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<HttpResponse<T>, R, GetObjectFromArrayBodyStepSupplier<T, R>> {

    private GetObjectFromArrayBodyStepSupplier(String description, Function<T, R[]> f) {
        super(description,  ((Function<HttpResponse<T>, T>) HttpResponse::body).andThen(t -> {
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
    public GetObjectFromArrayBodyStepSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public GetObjectFromArrayBodyStepSupplier<T, R> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    protected GetObjectFromArrayBodyStepSupplier<T, R> throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }
}
