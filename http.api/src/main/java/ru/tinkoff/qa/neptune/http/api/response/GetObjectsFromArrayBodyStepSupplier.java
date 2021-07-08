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
 * Builds a step-function that retrieves an array from http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of an item of resulted array
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted array")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
public abstract class GetObjectsFromArrayBodyStepSupplier<T, R, S extends GetObjectsFromArrayBodyStepSupplier<T, R, S>>
        extends SequentialGetStepSupplier.GetArrayStepSupplier<HttpStepContext, R, S> {

    private GetObjectsFromArrayBodyStepSupplier(Function<HttpStepContext, R[]> f) {
        super(f);
        addIgnored(Exception.class);
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayWhenResponseReceived}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description is a description of resulted array
     * @param received    is a received http response
     * @param f           is a function that describes how to get an array from a body of http response
     * @param <T>         is a type of a response body
     * @param <R>         is a type of an item of resulted array
     * @return an instance of {@link GetObjectsFromArrayWhenResponseReceived}
     */
    @Description("{description}")
    public static <T, R> GetObjectsFromArrayWhenResponseReceived<T, R> asArray(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            HttpResponse<T> received,
            Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromArrayWhenResponseReceived<>(received, f);
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayWhenResponseReceiving}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description    is a description of resulted array
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an array from a body of http response
     * @param <T>            is a type of a response body
     * @param <R>            is a type of an item of resulted array
     * @return an instance of {@link GetObjectsFromArrayWhenResponseReceiving}
     */
    @Description("{description}")
    public static <T, R> GetObjectsFromArrayWhenResponseReceiving<T, R> asArray(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<T> handler,
            Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromArrayWhenResponseReceiving<>(response(requestBuilder, handler),
                f);
    }


    /**
     * Creates an instance of {@link GetObjectsFromArrayWhenResponseReceived}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description is a description of resulted array
     * @param received    is a received http response
     * @param <R>         is a type of an item of array of response body
     * @return an instance of {@link GetObjectsFromArrayWhenResponseReceived}
     */
    @Description("{description}")
    public static <R> GetObjectsFromArrayWhenResponseReceived<R[], R> asArray(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            HttpResponse<R[]> received) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromArrayWhenResponseReceived<>(received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayWhenResponseReceiving}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description    is a description of resulted array
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of an item of array of response body
     * @return an instance of {@link GetObjectsFromArrayWhenResponseReceiving}
     */
    @Description("{description}")
    public static <R> GetObjectsFromArrayWhenResponseReceiving<R[], R> asArray(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<R[]> handler) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromArrayWhenResponseReceiving<>(response(requestBuilder, handler),
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
     * Returns an array from a body of http response which is received already.
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of an item of resulted array
     */
    @SuppressWarnings("unused")
    @DefineGetImperativeParameterName(value = "From http response get:")
    public static final class GetObjectsFromArrayWhenResponseReceived<T, R>
            extends GetObjectsFromArrayBodyStepSupplier<T, R, GetObjectsFromArrayWhenResponseReceived<T, R>> {

        @StepParameter("From body of received http response")
        final HttpResponse<T> response;

        private GetObjectsFromArrayWhenResponseReceived(HttpResponse<T> response,
                                                        Function<T, R[]> f) {
            super(f.compose(ignored -> response.body()));
            checkNotNull(response);
            this.response = response;
        }
    }

    /**
     * Returns an array from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of an item of resulted array
     */
    @SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
    @DefineGetImperativeParameterName(value = "Send http request. Wait for the response and then get:")
    public static final class GetObjectsFromArrayWhenResponseReceiving<T, R>
            extends GetObjectsFromArrayBodyStepSupplier<T, R, GetObjectsFromArrayWhenResponseReceiving<T, R>> {

        private final ResponseExecutionInfo info;
        private final ResponseSequentialGetSupplier<T> getResponse;

        private GetObjectsFromArrayWhenResponseReceiving(ReceiveResponseAndGetResultFunction<T, R[]> f) {
            super(f);
            var s = f.getGetResponseSupplier();
            info = s.getInfo();
            getResponse = s;
        }

        private GetObjectsFromArrayWhenResponseReceiving(ResponseSequentialGetSupplier<T> getResponse,
                                                         Function<T, R[]> f) {
            this(new ReceiveResponseAndGetResultFunction<>(f, getResponse));
        }

        /**
         * Defines time to receive a response and get desired data.
         *
         * @param timeOut is a time duration to receive a response and get desired data
         * @return self-reference
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        public GetObjectsFromArrayWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            return super.timeOut(timeOut);
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
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
        public GetObjectsFromArrayWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
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
        public GetObjectsFromArrayWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
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
        protected void onSuccess(R[] rs) {
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
        public GetObjectsFromArrayWhenResponseReceiving<T, R> throwOnNoResult() {
            getResponse.throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}
