package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
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
 * Builds a step-function that retrieves an object from http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of resulted object
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
public abstract class GetObjectFromBodyStepSupplier<T, R, S extends GetObjectFromBodyStepSupplier<T, R, S>>
        extends SequentialGetStepSupplier.GetObjectStepSupplier<HttpStepContext, R, S> {

    private GetObjectFromBodyStepSupplier(Function<HttpStepContext, R> f) {
        super(f);
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
    @Description("{description}")
    public static <T, R> GetObjectWhenResponseReceived<T, R> asObject(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            HttpResponse<T> received,
            Function<T, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectWhenResponseReceived<>(received, f);
    }

    /**
     * Creates an instance of {@link GetObjectWhenResponseReceived}. It builds a step-function that retrieves
     * a body of http response.
     *
     * @param received is a received http response
     * @param <T>      is a type of a response body
     * @return an instance of {@link GetObjectWhenResponseReceived}
     */
    @Description("Body of http response")
    public static <T> GetObjectWhenResponseReceived<T, T> asIs(HttpResponse<T> received) {
        return new GetObjectWhenResponseReceived<>(received, t -> t);
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
    @Description("{description}")
    public static <T, R> GetObjectWhenResponseReceiving<T, R> asObject(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<T> handler,
            Function<T, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectWhenResponseReceiving<>(response(requestBuilder, handler),
                f);
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
    @Description("Body of http response")
    public static <T> GetObjectWhenResponseReceiving<T, T> asIs(RequestBuilder requestBuilder,
                                                                HttpResponse.BodyHandler<T> handler) {
        return new GetObjectWhenResponseReceiving<>(response(requestBuilder, handler),
                t -> t);
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
    public static final class GetObjectWhenResponseReceived<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, GetObjectWhenResponseReceived<T, R>> {

        @StepParameter("From body of received http response")
        final HttpResponse<T> response;

        private GetObjectWhenResponseReceived(HttpResponse<T> response, Function<T, R> f) {
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
    public static final class GetObjectWhenResponseReceiving<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, GetObjectWhenResponseReceiving<T, R>> {

        private final ResponseExecutionInfo info;
        private final ResponseSequentialGetSupplier<T> getResponse;

        private GetObjectWhenResponseReceiving(ReceiveResponseAndGetResultFunction<T, R> f) {
            super(f);
            var s = f.getGetResponseSupplier();
            info = s.getInfo();
            getResponse = s;
        }

        private GetObjectWhenResponseReceiving(ResponseSequentialGetSupplier<T> getResponse,
                                               Function<T, R> f) {
            this(new ReceiveResponseAndGetResultFunction<>(f, getResponse));
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

        @Override
        public GetObjectWhenResponseReceiving<T, R> throwOnNoResult() {
            getResponse.throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}
