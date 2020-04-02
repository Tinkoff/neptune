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
 * Builds a step-function that retrieves an {@link Iterable} from http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of an item of resulted iterable
 * @param <S> is a type of resulted iterable
 */
public class GetObjectsFromIterableBodyStepSupplier<T, R, S extends Iterable<R>>
        extends SequentialGetStepSupplier.GetIterableStepSupplier<HttpStepContext, S, R, GetObjectsFromIterableBodyStepSupplier<T, R, S>> {

    private GetObjectsFromIterableBodyStepSupplier(String description, Function<T, S> f) {
        super(description, new ForResponseBodyFunction<>(f));
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableBodyStepSupplier}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description is a description of resulted array {@link Iterable}
     * @param f           is a function that describes how to transform body of http response to {@link Iterable}
     * @param <T>         is a type of a response body
     * @param <R>         is a type of an item of resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetObjectsFromIterableBodyStepSupplier}
     */
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

    void throwWhenNothing(String exceptionMessage) {
        super.throwOnEmptyResult(new DataHasNotBeenReceivedExceptionSupplier(exceptionMessage, getOriginalFunction()));
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
