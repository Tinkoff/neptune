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
 * Builds a step-function that retrieves an {@link Iterable} from http response body.
 *
 * @param <T> is a type of a response body
 * @param <R> is a type of an item of resulted iterable
 * @param <S> is a type of resulted iterable
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
public abstract class GetObjectsFromIterableBodyStepSupplier<T, R, S extends Iterable<R>, Q extends GetObjectsFromIterableBodyStepSupplier<T, R, S, Q>>
        extends SequentialGetStepSupplier.GetIterableStepSupplier<HttpStepContext, S, R, Q> {

    private GetObjectsFromIterableBodyStepSupplier(Function<HttpStepContext, S> f) {
        super(f);
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableWhenResponseReceived}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description is a description of resulted {@link Iterable}
     * @param received    is a received http response
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>         is a type of a response body
     * @param <R>         is a type of an item of resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetObjectsFromIterableWhenResponseReceived}
     */
    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectsFromIterableWhenResponseReceived<T, R, S> asIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            HttpResponse<T> received,
            Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromIterableWhenResponseReceived<>(received, f);
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableWhenResponseReceiving}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description    is a description of resulted {@link Iterable}
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>            is a type of a response body
     * @param <R>            is a type of an item of resulted iterable
     * @param <S>            is a type of resulted iterable
     * @return an instance of {@link GetObjectsFromIterableWhenResponseReceiving}
     */
    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectsFromIterableWhenResponseReceiving<T, R, S> asIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<T> handler,
            Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromIterableWhenResponseReceiving<>(response(requestBuilder, handler)
                .addIgnored(Exception.class),
                f);
    }


    /**
     * Creates an instance of {@link GetObjectsFromIterableWhenResponseReceived}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description is a description of resulted {@link Iterable}
     * @param received    is a received http response
     * @param <R>         is a type of an element of an iterable of response body
     * @param <S>         if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectsFromIterableWhenResponseReceived}
     */
    @Description("{description}")
    public static <R, S extends Iterable<R>> GetObjectsFromIterableWhenResponseReceived<S, R, S> asIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            HttpResponse<S> received) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromIterableWhenResponseReceived<>(received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableWhenResponseReceiving}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description    is a description of resulted {@link Iterable}
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of an element of an iterable of response body
     * @param <S>            if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectsFromIterableWhenResponseReceiving}
     */
    @Description("{description}")
    public static <R, S extends Iterable<R>> GetObjectsFromIterableWhenResponseReceiving<S, R, S> asIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<S> handler) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectsFromIterableWhenResponseReceiving<>(response(requestBuilder, handler)
                .addIgnored(Exception.class),
                rs -> rs);
    }


    @Override
    public Q criteria(String description, Predicate<? super R> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public Q criteria(Criteria<? super R> criteria) {
        return super.criteria(criteria);
    }

    /**
     * Returns an {@link Iterable} from a body of http response which is received already.
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of an element of resulted iterable
     * @param <S> is a type of resulted iterable
     */
    @SuppressWarnings("unused")
    @DefineGetImperativeParameterName(value = "From http response get:")
    public static final class GetObjectsFromIterableWhenResponseReceived<T, R, S extends Iterable<R>>
            extends GetObjectsFromIterableBodyStepSupplier<T, R, S, GetObjectsFromIterableWhenResponseReceived<T, R, S>> {

        @StepParameter("From body of received http response")
        final HttpResponse<T> response;

        private GetObjectsFromIterableWhenResponseReceived(HttpResponse<T> response,
                                                           Function<T, S> f) {
            super(f.compose(ignored -> response.body()));
            checkNotNull(response);
            this.response = response;
        }
    }

    /**
     * Returns an {@link Iterable} from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of a response body
     * @param <R> is a type of an element of resulted iterable
     * @param <S> is a type of resulted iterable
     */
    @SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
    @DefineGetImperativeParameterName(value = "Send http request. Wait for the response and then get:")
    public static final class GetObjectsFromIterableWhenResponseReceiving<T, R, S extends Iterable<R>>
            extends GetObjectsFromIterableBodyStepSupplier<T, R, S, GetObjectsFromIterableWhenResponseReceiving<T, R, S>> {

        private final ResponseExecutionInfo info;
        private final ResponseSequentialGetSupplier<T> getResponse;

        private GetObjectsFromIterableWhenResponseReceiving(ReceiveResponseAndGetResultFunction<T, S> f) {
            super(f);
            var s = f.getGetResponseSupplier();
            info = s.getInfo();
            getResponse = s;
        }

        private GetObjectsFromIterableWhenResponseReceiving(ResponseSequentialGetSupplier<T> getResponse,
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
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> retryTimeOut(Duration timeOut) {
            return super.timeOut(timeOut);
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> pollingInterval(Duration pollingTime) {
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
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
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
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> responseCriteria(Criteria<HttpResponse<T>> criteria) {
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
        protected void onSuccess(S rs) {
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

