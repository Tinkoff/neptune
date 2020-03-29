package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Builds a step-function that retrieves an object from http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of resulted object
 */
public class GetObjectFromBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetObjectStepSupplier<HttpStepContext, R, GetObjectFromBodyStepSupplier<T, R>> {

    private GetObjectFromBodyStepSupplier(String description, Function<T, R> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    /**
     * Creates an instance of {@link GetObjectFromBodyStepSupplier}. It builds a step-function that retrieves
     * an object from http response body.
     *
     * @param description is a description of resulted object
     * @param f is a function that describes how to transform body of http response to resulted object
     * @param <T> is a type of a response body
     * @param <R> is a type of resulted object
     * @return an instance of {@link GetObjectFromBodyStepSupplier}
     */
    public static <T, R> GetObjectFromBodyStepSupplier<T, R> object(String description, Function<T, R> f) {
        return new GetObjectFromBodyStepSupplier<>(description, f);
    }

    @Override
    protected GetObjectFromBodyStepSupplier<T, R> criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    protected GetObjectFromBodyStepSupplier<T, R> criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    GetObjectFromBodyStepSupplier<T, R> throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
        return this;
    }

    @Override
    protected GetObjectFromBodyStepSupplier<T, R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected GetObjectFromBodyStepSupplier<T, R> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected ForResponseBodyFunction<T, R> getOriginalFunction() {
        return (ForResponseBodyFunction<T, R>) super.getOriginalFunction();
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
