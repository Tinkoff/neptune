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
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

/**
 * Builds a step-function that retrieves an object from http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of resulted object
 */
@SequentialGetStepSupplier.DefaultParameterNames(
        criteria = "Criteria for an object to get",
        timeOut = "Time to receive expected http response and get the result"
)
@MakeCaptureOnFinishing(typeOfCapture = Object.class)
public abstract class GetObjectFromBodyStepSupplier<T, R, S extends GetObjectFromBodyStepSupplier<T, R, S>>
        extends SequentialGetStepSupplier.GetObjectStepSupplier<HttpStepContext, R, S> {

    private GetObjectFromBodyStepSupplier(String description, Function<HttpStepContext, R> f) {
        super(/*description,*/ f);
    }

    /**
     * Creates an instance of {@link GetObjectWhenResponseReceived}. It builds a step-function that retrieves
     * an object from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get resulted object from a body of http response
     * @param <T>         is a type of a response body
     * @param <R>         is a type of resulted object
     * @return an instance of {@link GetObjectWhenResponseReceived}
     */
    public static <T, R> GetObjectWhenResponseReceived<T, R> asObject(String description,
                                                                      HttpResponse<T> received,
                                                                      Function<T, R> f) {
        return new GetObjectWhenResponseReceived<>(description, received, f);
    }

    /**
     * Creates an instance of {@link GetObjectWhenResponseReceived}. It builds a step-function that retrieves
     * a body of http response.
     *
     * @param received is a received http response
     * @param <T>      is a type of a response body
     * @return an instance of {@link GetObjectWhenResponseReceived}
     */
    public static <T> GetObjectWhenResponseReceived<T, T> asIs(HttpResponse<T> received) {
        return asObject("Body of http response", received, t -> t);
    }

    /**
     * Creates an instance of {@link GetObjectWhenResponseReceiving}. It builds a step-function that retrieves
     * an object from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get resulted object from a body of http response
     * @param <T>            is a type of a response body
     * @param <R>            is a type of resulted object
     * @return an instance of {@link GetObjectWhenResponseReceiving}
     */
    public static <T, R> GetObjectWhenResponseReceiving<T, R> asObject(String description,
                                                                       RequestBuilder requestBuilder,
                                                                       HttpResponse.BodyHandler<T> handler,
                                                                       Function<T, R> f) {
        return new GetObjectWhenResponseReceiving<>(description, response(requestBuilder, handler), f);
    }

    /**
     * Creates an instance of {@link GetObjectWhenResponseReceiving}. It builds a step-function that retrieves
     * a body of http response.
     *
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <T>            is a type of a response body
     * @return an instance of {@link GetObjectWhenResponseReceiving}
     */
    public static <T> GetObjectWhenResponseReceiving<T, T> asIs(RequestBuilder requestBuilder,
                                                                HttpResponse.BodyHandler<T> handler) {
        return asObject("Body of http response", requestBuilder, handler, t -> t);
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
    public static final class GetObjectWhenResponseReceived<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, GetObjectWhenResponseReceived<T, R>> {

        @StepParameter("From body of received http response")
        final HttpResponse<T> response;

        private GetObjectWhenResponseReceived(String description, HttpResponse<T> response, Function<T, R> f) {
            super(description, f.compose(ignored -> response.body()));
            checkNotNull(response);
            this.response = response;
        }
    }

    /**
     * Returns an object from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of a resulted object
     */
    public static final class GetObjectWhenResponseReceiving<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, GetObjectWhenResponseReceiving<T, R>> {

        private final ResponseExecutionInfo info;
        private final ResponseSequentialGetSupplier<T> getResponse;

        private GetObjectWhenResponseReceiving(String description, ReceiveResponseAndGetResultFunction<T, R> f) {
            super(description, f);
            var s = f.getGetResponseSupplier();
            info = s.getInfo();
            getResponse = s;
        }

        private GetObjectWhenResponseReceiving(String description,
                                               ResponseSequentialGetSupplier<T> getResponse,
                                               Function<T, R> f) {
            this(description, new ReceiveResponseAndGetResultFunction<>(f, getResponse));
        }

        /**
         * Defines time to receive a response and get desired data.
         *
         * @param timeOut is a time duration to receive a response and get desired data
         * @return self-reference
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        public GetObjectWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            return super.timeOut(timeOut);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
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
        public GetObjectWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
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
        public GetObjectWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
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
