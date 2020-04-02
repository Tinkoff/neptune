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
 * Builds a step-function that retrieves an object from some {@link Iterable} which is retrieved from
 * http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of resulted object
 */
@SuppressWarnings("unchecked")
public class GetObjectFromIterableBodyStepSupplier<T, R> extends
        SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<HttpStepContext, R, GetObjectFromIterableBodyStepSupplier<T, R>> {

    private <S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier(String description, Function<T, S> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableBodyStepSupplier}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param f           is a function that describes how to transform body of http response to iterable
     * @param <T>         is a type of a response body
     * @param <R>         is a type of resulted object
     * @param <S>         if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableBodyStepSupplier}
     */
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

    void throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
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
