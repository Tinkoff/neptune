package ru.tinkoff.qa.neptune.http.api.response;


import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class GetObjectFromIterableBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<HttpStepContext, R, GetObjectFromIterableBodyStepSupplier<T, R>> {

    private <S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier(String description, Function<T, S> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    public static <T, R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<T, R> oneOfIterable(String description, Function<T, S> f) {
        return new GetObjectFromIterableBodyStepSupplier<>(description, f);
    }

    @Override
    protected GetObjectFromIterableBodyStepSupplier<T, R> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    protected GetObjectFromIterableBodyStepSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected GetObjectFromIterableBodyStepSupplier<T, R> throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }

    @Override
    protected GetObjectFromIterableBodyStepSupplier<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected ForResponseBodyFunction<T, ? extends Iterable<R>> getOriginalFunction() {
        return (ForResponseBodyFunction<T, ? extends Iterable<R>>) super.getOriginalFunction();
    }

    @Override
    public Function<HttpStepContext, R> get() {
        return getEndFunction();
    }
}
