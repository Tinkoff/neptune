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
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.executionResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.responseResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionResultMatches.resultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplierInternal.responseInternal;

/**
 * Builds a step-function that retrieves an object from http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public abstract class GetObjectFromBodyStepSupplier<T, R, M, S extends GetObjectFromBodyStepSupplier<T, R, M, S>>
        extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HttpStepContext, R, M, S> {

    private GetObjectFromBodyStepSupplier(Function<M, R> f) {
        super(f);
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
        return new GetObjectWhenResponseReceiving<>(responseInternal(translate(description), requestBuilder, handler, f, r -> true));
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
        return new GetObjectWhenResponseReceiving<>(responseInternal(requestBuilder, handler));
    }

    /**
     * Returns an object from a body of http response which is received already.
     *
     * @param <T> is a type of response body
     * @param <R> is a type of resulted object
     */
    @SuppressWarnings("unused")
    @DefineGetImperativeParameterName(value = "From http response get:")
    @DefineFromParameterName("Response")
    public static final class GetObjectWhenResponseReceived<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, HttpResponse<T>, GetObjectWhenResponseReceived<T, R>> {

        private GetObjectWhenResponseReceived(HttpResponse<T> response, Function<T, R> f) {
            super(f.compose(HttpResponse::body));
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
    public static final class GetObjectWhenResponseReceiving<T, R>
            extends GetObjectFromBodyStepSupplier<T, R, ResponseExecutionResult<T, R>, GetObjectWhenResponseReceiving<T, R>> {

        private GetObjectWhenResponseReceiving(ResponseSequentialGetSupplierInternal<T, R> getResponse) {
            super(ResponseExecutionResult::getResult);
            from(getResponse.addIgnored(Exception.class));
        }

        /**
         * Defines time to receive a response and get desired data.
         *
         * @param timeOut is a time duration to receive a response and get desired data
         * @return self-reference
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        public GetObjectWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).timeOut(timeOut);
            return this;
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).pollingInterval(pollingTime);
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
            return responseCriteria(condition(description, predicate));
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R>) getFrom()).criteria(responseResultMatches(criteria));
            return this;
        }

        /**
         * Defines OR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteriaOr(Criteria<HttpResponse<T>>... criteria) {
            return responseCriteria(OR(criteria));
        }

        /**
         * Defines XOR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteriaOnlyOne(Criteria<HttpResponse<T>>... criteria) {
            return responseCriteria(ONLY_ONE(criteria));
        }

        /**
         * Defines NOT-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectWhenResponseReceiving<T, R> responseCriteriaNot(Criteria<HttpResponse<T>>... criteria) {
            return responseCriteria(NOT(criteria));
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteriaOr(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R>) getFrom()).criteria(executionResultMatches(resultMatches(OR(criteria))));
            return super.criteriaOr(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteriaOnlyOne(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R>) getFrom()).criteria(executionResultMatches(resultMatches(ONLY_ONE(criteria))));
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteriaNot(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R>) getFrom()).criteria(executionResultMatches(resultMatches(NOT(criteria))));
            return super.criteriaNot(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteria(Criteria<? super R> criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R>) getFrom()).criteria(executionResultMatches(resultMatches(criteria)));
            return super.criteria(criteria);
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> criteria(String description, Predicate<? super R> criteria) {
            return criteria(condition(description, criteria));
        }

        @Override
        public GetObjectWhenResponseReceiving<T, R> throwOnNoResult() {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}
