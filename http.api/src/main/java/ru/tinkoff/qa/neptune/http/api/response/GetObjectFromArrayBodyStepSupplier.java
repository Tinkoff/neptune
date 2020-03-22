package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

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

    protected GetObjectFromArrayBodyStepSupplier<T, R> throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
        return this;
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

    @Override
    protected String prepareStepDescription() {
        return super.prepareStepDescription();
    }
}