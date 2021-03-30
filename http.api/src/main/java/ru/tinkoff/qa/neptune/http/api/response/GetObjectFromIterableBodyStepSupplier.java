package ru.tinkoff.qa.neptune.http.api.response;


import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Set.of;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

/**
 * Builds a step-function that retrieves an object from some {@link Iterable} which is retrieved from
 * http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of resulted object
 */
@SequentialGetStepSupplier.DefaultParameterNames(
        criteria = "Criteria for an element of the iterable",
        timeOut = "Time to receive expected http response and get the result"
)
@MakeCaptureOnFinishing(typeOfCapture = Object.class)
public abstract class GetObjectFromIterableBodyStepSupplier<T, R, S extends GetObjectFromIterableBodyStepSupplier<T, R, S>>
        extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<HttpStepContext, R, S> {

    private <Q extends Iterable<R>> GetObjectFromIterableBodyStepSupplier(Function<HttpStepContext, Q> f) {
        super(f);
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>         is a type of a response body
     * @param <R>         is a type of resulted object
     * @param <S>         if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived<T, R> asOneOfIterable(String description,
                                                                                                                HttpResponse<T> received,
                                                                                                                Function<T, S> f) {
        return new GetObjectFromIterableWhenResponseReceived<>(received, f).setDescription(translate(description));
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>            is a type of a response body
     * @param <R>            is a type of resulted object
     * @param <S>            if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving<T, R> asOneOfIterable(String description,
                                                                                                                 RequestBuilder requestBuilder,
                                                                                                                 HttpResponse.BodyHandler<T> handler,
                                                                                                                 Function<T, S> f) {
        return new GetObjectFromIterableWhenResponseReceiving<>(response(requestBuilder, handler)
                .addIgnored(Exception.class),
                f)
                .setDescription(translate(description));
    }


    /**
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param <R>         is a type of an element of an iterable of response body
     * @param <S>         if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    public static <R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived<S, R> asOneOfIterable(String description,
                                                                                                             HttpResponse<S> received) {
        return new GetObjectFromIterableWhenResponseReceived<>(received, rs -> rs).setDescription(translate(description));
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of an element of an iterable of response body
     * @param <S>            if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    public static <R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving<S, R> asOneOfIterable(String description,
                                                                                                              RequestBuilder requestBuilder,
                                                                                                              HttpResponse.BodyHandler<S> handler) {
        return new GetObjectFromIterableWhenResponseReceiving<>(response(requestBuilder, handler)
                .addIgnored(Exception.class),
                rs -> rs)
                .setDescription(translate(description));
    }


    @Override
    public S criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    @Override
    public S criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    /**
     * Make throw an exception if no data received
     *
     * @param exceptionMessage is a message of {@link DesiredDataHasNotBeenReceivedException} exception to be thrown
     * @return self-reference
     * @see SequentialGetStepSupplier#throwOnEmptyResult(Supplier)
     */
    public S throwIfNoDesiredDataReceived(String exceptionMessage) {
        return super.throwOnEmptyResult(() -> new DesiredDataHasNotBeenReceivedException(exceptionMessage));
    }

    /**
     * Returns an object from a body of http response which is received already.
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of resulted object
     */
    @SuppressWarnings("unused")
    public static final class GetObjectFromIterableWhenResponseReceived<T, R>
            extends GetObjectFromIterableBodyStepSupplier<T, R, GetObjectFromIterableWhenResponseReceived<T, R>> {

        @StepParameter("From body of received http response")
        final HttpResponse<T> response;

        private <S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived(HttpResponse<T> response,
                                                                                  Function<T, S> f) {
            super(f.compose(ignored -> response.body()));
            checkNotNull(response);
            this.response = response;
        }

        @Override
        protected GetObjectFromIterableWhenResponseReceived<T, R> setDescription(String description) {
            return super.setDescription(description);
        }
    }

    /**
     * Returns an object from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of a resulted object
     */
    public static final class GetObjectFromIterableWhenResponseReceiving<T, R>
            extends GetObjectFromIterableBodyStepSupplier<T, R, GetObjectFromIterableWhenResponseReceiving<T, R>> {

        private final ResponseExecutionInfo info;
        private final ResponseSequentialGetSupplier<T> getResponse;

        private <S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving(ReceiveResponseAndGetResultFunction<T, S> f) {
            super(f);
            var s = f.getGetResponseSupplier();
            info = s.getInfo();
            getResponse = s;
        }

        private <S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving(ResponseSequentialGetSupplier<T> getResponse,
                                                                                   Function<T, S> f) {
            this(new ReceiveResponseAndGetResultFunction<>(f, getResponse));
        }

        @Override
        protected GetObjectFromIterableWhenResponseReceiving<T, R> setDescription(String description) {
            return super.setDescription(description);
        }

        /**
         * Defines time to receive a response and get desired data.
         *
         * @param timeOut is a time duration to receive a response and get desired data
         * @return self-reference
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            return super.timeOut(timeOut);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
            return super.pollingInterval(pollingTime);
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param description criteria description
         * @param predicate   is how to match http response
         * @return self-reference
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
            getResponse.criteria(description, predicate);
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            getResponse.criteria(criteria);
            return this;
        }

        @Override
        protected Function<HttpStepContext, R> getEndFunction() {
            return httpStepContext -> {
                boolean success = false;
                try {
                    catchValue(getResponse.getRequest().body(), of(new CaptorFilterByProducedType(Object.class)));
                    var result = super.getEndFunction().apply(httpStepContext);
                    success = true;
                    return result;
                } finally {
                    if ((success && catchSuccessEvent()) || (!success && catchFailureEvent())) {
                        catchValue(info, of(new CaptorFilterByProducedType(Object.class)));
                        catchValue(info.getLastReceived(), of(new CaptorFilterByProducedType(Object.class)));
                    }
                }
            };
        }
    }
}
