package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.captors.request.AbstractRequestBodyCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectsCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.RequestResponseLogCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.ResponseCaptor;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

/**
 * Builds a step-function that retrieves an object from some {@link Iterable} which is retrieved from
 * http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of resulted object
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
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
    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived<T, R> asOneOfIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = DescriptionTranslationGetter.class) String description,
            HttpResponse<T> received,
            Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableWhenResponseReceived<>(received, f);
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
    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving<T, R> asOneOfIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = DescriptionTranslationGetter.class) String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<T> handler,
            Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableWhenResponseReceiving<>(response(requestBuilder, handler)
                .addIgnored(Exception.class),
                f);
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
    @Description("{description}")
    public static <R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived<S, R> asOneOfIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = DescriptionTranslationGetter.class) String description,
            HttpResponse<S> received) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableWhenResponseReceived<>(received, rs -> rs);
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
    @Description("{description}")
    public static <R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving<S, R> asOneOfIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = DescriptionTranslationGetter.class) String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<S> handler) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableWhenResponseReceiving<>(response(requestBuilder, handler)
                .addIgnored(Exception.class),
                rs -> rs);
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
     * Returns an object from a body of http response which is received already.
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of resulted object
     */
    @SuppressWarnings("unused")
    @DefineGetImperativeParameterName(value = "From http response get:")
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
    }

    /**
     * Returns an object from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of a resulted object
     */
    @SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
    @DefineGetImperativeParameterName(value = "Send http request. Wait for the response and then get:")
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
        public Map<String, String> getParameters() {
            var params = super.getParameters();
            params.putAll(getResponse.getParameters());
            return params;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onStart(HttpStepContext httpStepContext) {
            catchValue(getResponse.getRequest().body(), createCaptors(new Class[]{AbstractRequestBodyCaptor.class}));
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onSuccess(R r) {
            if (catchSuccessEvent()) {
                catchValue(info, createCaptors(new Class[]{RequestResponseLogCaptor.class}));
                catchValue(info.getLastReceived(), createCaptors(new Class[]{ResponseCaptor.class,
                        AbstractResponseBodyObjectCaptor.class,
                        AbstractResponseBodyObjectsCaptor.class}));
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onFailure(HttpStepContext httpStepContext, Throwable throwable) {
            if (catchFailureEvent()) {
                catchValue(info, createCaptors(new Class[]{RequestResponseLogCaptor.class}));
                catchValue(info.getLastReceived(), createCaptors(new Class[]{ResponseCaptor.class,
                        AbstractResponseBodyObjectCaptor.class,
                        AbstractResponseBodyObjectsCaptor.class}));
            }
        }
    }
}
