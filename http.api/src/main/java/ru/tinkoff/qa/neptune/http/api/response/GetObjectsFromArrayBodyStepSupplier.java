package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

public class GetObjectsFromArrayBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetArrayStepSupplier<HttpStepContext, R, GetObjectsFromArrayBodyStepSupplier<T, R>> {

    private GetObjectsFromArrayBodyStepSupplier(String description, Function<T, R[]> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    public static <T, R> GetObjectsFromArrayBodyStepSupplier<T, R> array(String description, Function<T, R[]> f) {
        return new GetObjectsFromArrayBodyStepSupplier<>(description, f);
    }

    @Override
    protected GetObjectsFromArrayBodyStepSupplier<T, R> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    protected GetObjectsFromArrayBodyStepSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    protected GetObjectsFromArrayBodyStepSupplier<T, R> throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
        return this;
    }

    @Override
    protected GetObjectsFromArrayBodyStepSupplier<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected GetObjectsFromArrayBodyStepSupplier<T, R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected ForResponseBodyFunction<T, R[]> getOriginalFunction() {
        return (ForResponseBodyFunction<T, R[]>) super.getOriginalFunction();
    }

    @Override
    public Function<HttpStepContext, R[]> get() {
        return getEndFunction();
    }

    @Override
    protected String prepareStepDescription() {
        return super.prepareStepDescription();
    }
}
