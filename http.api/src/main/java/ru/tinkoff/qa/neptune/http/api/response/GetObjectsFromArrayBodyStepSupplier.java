package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class GetObjectsFromArrayBodyStepSupplier<T, R> extends SequentialGetStepSupplier.GetArrayStepSupplier<HttpResponse<T>, R, GetObjectsFromArrayBodyStepSupplier<T, R>> {

    private GetObjectsFromArrayBodyStepSupplier(String description, Function<T, R[]> f) {
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
    public GetObjectsFromArrayBodyStepSupplier<T, R> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    public GetObjectsFromArrayBodyStepSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected GetObjectsFromArrayBodyStepSupplier<T, R> throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }
}
