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
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.arrayResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.responseResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplierInternal.responseInternal;

/**
 * It builds a step-function that retrieves an object from array which is retrieved from
 * http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public abstract class GetObjectFromArrayBodyStepSupplier<T, R, M, S extends GetObjectFromArrayBodyStepSupplier<T, R, M, S>>
        extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<HttpStepContext, R, M, S> {

    private GetObjectFromArrayBodyStepSupplier(Function<M, R[]> f) {
        super(f);
        addIgnored(Exception.class);
    }

    /**
     * Creates an instance of {@link GetObjectFromArrayWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get an array from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of resulted object
     * @return an instance of {@link GetObjectFromArrayWhenResponseReceived}
     */
    @Description("{description}")
    public static <T, R> GetObjectFromArrayWhenResponseReceived<T, R> asOneOfArray(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            HttpResponse<T> received,
            Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromArrayWhenResponseReceived<>(received, f);
    }

    /**
     * Creates an instance of {@link GetObjectFromArrayWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an array from a body of http response
     * @param <T>            is a type of response body
     * @param <R>            is a type of resulted object
     * @return an instance of {@link GetObjectFromArrayWhenResponseReceived}
     */
    @Description("{description}")
    public static <T, R> GetObjectFromArrayWhenResponseReceiving<T, R> asOneOfArray(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<T> handler,
            Function<T, R[]> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromArrayWhenResponseReceiving<>(responseInternal(translate(description),
                requestBuilder,
                handler,
                f, rs -> rs.length > 0));
    }


    /**
     * Creates an instance of {@link GetObjectFromArrayWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param <R>         is a type of an item of array of response body
     * @return an instance of {@link GetObjectFromArrayWhenResponseReceived}
     */
    public static <R> GetObjectFromArrayWhenResponseReceived<R[], R> asOneOfArray(
            String description,
            HttpResponse<R[]> received) {
        return asOneOfArray(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectFromArrayWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * array which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of an item of array of response body
     * @return an instance of {@link GetObjectFromArrayWhenResponseReceived}
     */
    public static <R> GetObjectFromArrayWhenResponseReceiving<R[], R> asOneOfArray(
            String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<R[]> handler) {
        return asOneOfArray(description, requestBuilder, handler, rs -> rs);
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
    public static final class GetObjectFromArrayWhenResponseReceived<T, R>
            extends GetObjectFromArrayBodyStepSupplier<T, R, HttpResponse<T>, GetObjectFromArrayWhenResponseReceived<T, R>> {

        private GetObjectFromArrayWhenResponseReceived(HttpResponse<T> response, Function<T, R[]> f) {
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
    public static final class GetObjectFromArrayWhenResponseReceiving<T, R>
            extends GetObjectFromArrayBodyStepSupplier<T, R, ResponseExecutionResult<T, R[]>, GetObjectFromArrayWhenResponseReceiving<T, R>> {

        private GetObjectFromArrayWhenResponseReceiving(ResponseSequentialGetSupplierInternal<T, R[]> getResponse) {
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
        public GetObjectFromArrayWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).timeOut(timeOut);
            return this;
        }

        @Override
        public GetObjectFromArrayWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
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
        public GetObjectFromArrayWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(responseResultMatches(description, predicate));
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromArrayWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(responseResultMatches(criteria));
            return this;
        }

        /**
         * Defines OR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromArrayWhenResponseReceiving<T, R> responseCriteriaOr(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(responseResultMatches(OR(criteria)));
            return this;
        }

        /**
         * Defines XOR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromArrayWhenResponseReceiving<T, R> responseCriteriaOnlyOne(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(responseResultMatches(ONLY_ONE(criteria)));
            return this;
        }

        /**
         * Defines NOT-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromArrayWhenResponseReceiving<T, R> responseCriteriaNot(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(responseResultMatches(NOT(criteria)));
            return this;
        }

        @Override
        public GetObjectFromArrayWhenResponseReceiving<T, R> criteriaOr(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(arrayResultMatches(new ResponseExecutionResultHasItems<>(OR(criteria))));
            return super.criteriaOr(criteria);
        }

        @Override
        public GetObjectFromArrayWhenResponseReceiving<T, R> criteriaOnlyOne(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(arrayResultMatches(new ResponseExecutionResultHasItems<>(ONLY_ONE(criteria))));
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public GetObjectFromArrayWhenResponseReceiving<T, R> criteriaNot(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(arrayResultMatches(new ResponseExecutionResultHasItems<>(NOT(criteria))));
            return super.criteriaNot(criteria);
        }

        @Override
        public GetObjectFromArrayWhenResponseReceiving<T, R> criteria(Criteria<? super R> criteria) {
            ((ResponseSequentialGetSupplierInternal<T, R[]>) getFrom()).criteria(arrayResultMatches(new ResponseExecutionResultHasItems<>(criteria)));
            return super.criteria(criteria);
        }

        @Override
        public GetObjectFromArrayWhenResponseReceiving<T, R> criteria(String description, Predicate<? super R> criteria) {
            return criteria(condition(translate(description), criteria));
        }

        @Override
        public GetObjectFromArrayWhenResponseReceiving<T, R> throwOnNoResult() {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}