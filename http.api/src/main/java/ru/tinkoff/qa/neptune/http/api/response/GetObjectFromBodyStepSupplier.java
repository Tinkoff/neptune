package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplierInternal.responseInternal;
import static ru.tinkoff.qa.neptune.http.api.response.ResultCriteria.bodyMatches;

/**
 * Builds a step-function that retrieves an object from http response body.
 *
 * @param <T> is a type of response body
 * @param <R> is a type of resulted object
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public abstract class GetObjectFromBodyStepSupplier<T, R, S extends GetObjectFromBodyStepSupplier<T, R, S>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, S> {

    private GetObjectFromBodyStepSupplier(Function<T, R> f) {
        super(((Function<HttpResponse<T>, T>) HttpResponse::body).andThen(f));
        addIgnored(Exception.class);
    }

    /**
     * Creates an instance of {@link GetObjectWhenResponseReceived}. It builds a step-function that retrieves
     * an object from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get resulted object from a body of http response
     * @param <T>         is a type of response body
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
     * @param <T>      is a type of response body
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
     * @param <T>            is a type of response body
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
        return new GetObjectWhenResponseReceiving<>(responseInternal(requestBuilder, handler),
                f);
    }

    /**
     * Creates an instance of {@link GetObjectWhenResponseReceiving}. It builds a step-function that retrieves
     * a body of http response.
     *
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <T>            is a type of response body
     * @return an instance of {@link GetObjectWhenResponseReceiving}
     */
    @Description("Body of http response")
    public static <T> GetObjectWhenResponseReceiving<T, T> asIs(RequestBuilder requestBuilder,
                                                                HttpResponse.BodyHandler<T> handler) {
        return new GetObjectWhenResponseReceiving<>(responseInternal(requestBuilder, handler),
                t -> t);
    }

    /**
     * Returns an object from a body of http response which is received already.
     *
     * @param <T> is a type of response body
     * @param <R> is a type of resulted object
     */
    @SuppressWarnings("unused")
    @DefineGetImperativeParameterName(value = "From http response get:")
    @DefineFromParameterName("From body of received http response")
    public static final class GetObjectWhenResponseReceived<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, GetObjectWhenResponseReceived<T, R>> {

        private GetObjectWhenResponseReceived(HttpResponse<T> response, Function<T, R> f) {
            super(f);
            checkNotNull(response);
            from(response);
        }
    }

    /**
     * Returns an object from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of response body
     * @param <R> is a type of resulted object
     */
    @SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
    @DefineGetImperativeParameterName(value = "Send http request. Wait for the response and then get:")
    public static final class GetObjectWhenResponseReceiving<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, GetObjectWhenResponseReceiving<T, R>> {

        private final Function<T, R> f;

        private GetObjectWhenResponseReceiving(ResponseSequentialGetSupplierInternal<T> getResponse,
                                               Function<T, R> f) {
            super(f);
            from(getResponse.addIgnored(Exception.class));
            this.f = f;
        }

        /**
         * Defines time to receive a response and get desired data.
         *
         * @param timeOut is a time duration to receive a response and get desired data
         * @return self-reference
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        public GetObjectWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).timeOut(timeOut);
            return this;
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).pollingInterval(pollingTime);
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param description criteria description
         * @param predicate   is how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(description, predicate);
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(criteria);
            return this;
        }

        /**
         * Defines OR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteriaOr(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaOr(criteria);
            return this;
        }

        /**
         * Defines XOR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteriaOnlyOne(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaOnlyOne(criteria);
            return this;
        }

        /**
         * Defines NOT-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteriaNot(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaNot(criteria);
            return this;
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteriaOr(Criteria<? super R>... criteria) {
            var orCriteria = OR(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(bodyMatches(new BodyMatches(orCriteria), f, orCriteria));
            return super.criteriaOr(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteriaOnlyOne(Criteria<? super R>... criteria) {
            var xorCriteria = ONLY_ONE(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(bodyMatches(new BodyMatches(xorCriteria), f, xorCriteria));
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteriaNot(Criteria<? super R>... criteria) {
            var notCriteria = NOT(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(bodyMatches(new BodyMatches(notCriteria), f, notCriteria));
            return super.criteriaNot(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteria(Criteria<? super R> criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(bodyMatches(new BodyMatches(criteria), f, criteria));
            return super.criteria(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteria(String description, Predicate<? super R> criteria) {
            return criteria(condition(translate(description), criteria));
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> throwOnNoResult() {
            ((ResponseSequentialGetSupplierInternal<?>) getFrom()).throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}
