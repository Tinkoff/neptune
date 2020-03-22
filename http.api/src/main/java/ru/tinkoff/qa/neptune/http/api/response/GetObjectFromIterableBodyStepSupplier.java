package ru.tinkoff.qa.neptune.http.api.response;


import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

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

    protected GetObjectFromIterableBodyStepSupplier<T, R> throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
        return this;
    }

    @Override
    protected GetObjectFromIterableBodyStepSupplier<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected GetObjectFromIterableBodyStepSupplier<T, R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected ForResponseBodyFunction<T, ? extends Iterable<R>> getOriginalFunction() {
        return (ForResponseBodyFunction<T, ? extends Iterable<R>>) super.getOriginalFunction();
    }

    @Override
    public Function<HttpStepContext, R> get() {
        return getEndFunction();
    }

    @Override
    protected String prepareStepDescription() {
        return super.prepareStepDescription();
    }
}
