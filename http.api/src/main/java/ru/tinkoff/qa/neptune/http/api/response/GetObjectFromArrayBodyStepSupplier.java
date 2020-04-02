package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;
import static java.util.Set.of;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

/**
 * It builds a step-function that retrieves an object from array which is retrieved from
 * http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of an item of resulted object
 */
public final class GetObjectFromArrayBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<HttpStepContext, R, GetObjectFromArrayBodyStepSupplier<T, R>> {

    private GetObjectFromArrayBodyStepSupplier(String description, Function<T, R[]> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    /**
     * Creates an instance of {@link GetObjectFromArrayBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param f           is a function that describes how to transform body of http response to array
     * @param <T>         is a type of a response body
     * @param <R>         is a type of resulted object
     * @return an instance of {@link GetObjectFromArrayBodyStepSupplier}
     */
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

    void throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
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
        return httpStepContext -> {
            var success = false;
            try {
                var result = getEndFunction().apply(httpStepContext);
                success = true;
                return result;
            } finally {
                var f = getOriginalFunction();
                var captureBy = of(new CaptorFilterByProducedType(Object.class));
                if (success && catchSuccessEvent()) {
                    catchValue(f.getLastValidResponse(), captureBy);
                    catchValue(f.getLog(), captureBy);
                }
                if (!success && catchFailureEvent()) {
                    var r = ofNullable(f.getLastValidResponse()).orElseGet(f::getLastReceivedResponse);
                    catchValue(r, captureBy);
                    catchValue(f.getLog(), captureBy);
                }
            }
        };
    }

    @Override
    protected String prepareStepDescription() {
        return super.prepareStepDescription();
    }
}