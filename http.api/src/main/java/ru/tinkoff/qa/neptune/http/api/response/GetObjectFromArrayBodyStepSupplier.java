package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class GetObjectFromArrayBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<HttpStepContext, R, GetObjectFromArrayBodyStepSupplier<T, R>> {

    private GetObjectFromArrayBodyStepSupplier(String description, Function<T, R[]> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    public static <T, R> GetObjectFromArrayBodyStepSupplier<T, R> oneOfArray(String description, Function<T, R[]> f) {
        return new GetObjectFromArrayBodyStepSupplier<>(description, f);
    }

    @Override
    protected GetObjectFromArrayBodyStepSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected GetObjectFromArrayBodyStepSupplier<T, R> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    protected GetObjectFromArrayBodyStepSupplier<T, R> throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }

    @Override
    protected GetObjectFromArrayBodyStepSupplier<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected GetObjectFromArrayBodyStepSupplier<T, R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected ForResponseBodyFunction<T, R[]> getOriginalFunction() {
        return (ForResponseBodyFunction<T, R[]>) super.getOriginalFunction();
    }

    @Override
    public Function<HttpStepContext, R> get() {
        return getEndFunction();
    }
}