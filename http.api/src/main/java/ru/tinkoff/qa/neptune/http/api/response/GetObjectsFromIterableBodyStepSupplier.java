package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

public class GetObjectsFromIterableBodyStepSupplier<T, R, S extends Iterable<R>>
        extends SequentialGetStepSupplier.GetIterableStepSupplier<HttpStepContext, S, R, GetObjectsFromIterableBodyStepSupplier<T, R, S>> {

    private GetObjectsFromIterableBodyStepSupplier(String description, Function<T, S> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    public static <T, R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable(String description, Function<T, S> f) {
        return new GetObjectsFromIterableBodyStepSupplier<>(description, f);
    }

    @Override
    protected GetObjectsFromIterableBodyStepSupplier<T, R, S> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected GetObjectsFromIterableBodyStepSupplier<T, R, S> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    protected GetObjectsFromIterableBodyStepSupplier<T, R, S> throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
        return this;
    }

    @Override
    protected GetObjectsFromIterableBodyStepSupplier<T, R, S> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected GetObjectsFromIterableBodyStepSupplier<T, R, S> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected ForResponseBodyFunction<T, S> getOriginalFunction() {
        return (ForResponseBodyFunction<T, S>) super.getOriginalFunction();
    }

    @Override
    public Function<HttpStepContext, S> get() {
        return getEndFunction();
    }

    @Override
    protected String prepareStepDescription() {
        return super.prepareStepDescription();
    }
}
