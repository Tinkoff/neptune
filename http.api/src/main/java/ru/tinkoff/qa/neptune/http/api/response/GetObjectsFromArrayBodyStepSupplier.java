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
import static ru.tinkoff.qa.neptune.http.api.response.ResultCriteria.arrayBodyMatches;

/**
 * Builds a step-function that retrieves an array from http response body.
 *
 * @param <T> is a type of response body
 * @param <R> is a type of item of resulted array
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted array")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public abstract class GetObjectsFromArrayBodyStepSupplier<T, R, S extends GetObjectsFromArrayBodyStepSupplier<T, R, S>>
        extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, S> {

    private GetObjectsFromArrayBodyStepSupplier(Function<T, R[]> f) {
        super(((Function<HttpResponse<T>, T>) HttpResponse::body).andThen(f));
        addIgnored(Exception.class);
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayWhenResponseReceived}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description is a description of resulted array
     * @param received    is a received http response
     * @param f           is a function that describes how to get an array from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of item of resulted array
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
     * @param <T>            is a type of response body
     * @param <R>            is a type of item of resulted array
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
        return new GetObjectsFromArrayWhenResponseReceiving<>(responseInternal(requestBuilder, handler),
                f);
    }


    /**
     * Creates an instance of {@link GetObjectsFromArrayWhenResponseReceived}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description is a description of resulted array
     * @param received    is a received http response
     * @param <R>         is a type of item of array of response body
     * @return an instance of {@link GetObjectsFromArrayWhenResponseReceived}
     */
    public static <R> GetObjectsFromArrayWhenResponseReceived<R[], R> asArray(
            String description,
            HttpResponse<R[]> received) {
        return asArray(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectsFromArrayWhenResponseReceiving}. It builds a step-function that retrieves
     * an array from http response body.
     *
     * @param description    is a description of resulted array
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of item of array of response body
     * @return an instance of {@link GetObjectsFromArrayWhenResponseReceiving}
     */
    public static <R> GetObjectsFromArrayWhenResponseReceiving<R[], R> asArray(
            String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<R[]> handler) {
        return asArray(description, requestBuilder, handler, rs -> rs);
    }

    /**
     * Returns an array from a body of http response which is received already.
     *
     * @param <T> is a type of response body
     * @param <R> is a type of item of resulted array
     */
    @SuppressWarnings("unused")
    @DefineGetImperativeParameterName(value = "From http response get:")
    @DefineFromParameterName("From body of received http response")
    public static final class GetObjectsFromArrayWhenResponseReceived<T, R>
            extends GetObjectsFromArrayBodyStepSupplier<T, R, GetObjectsFromArrayWhenResponseReceived<T, R>> {

        private GetObjectsFromArrayWhenResponseReceived(HttpResponse<T> response,
                                                        Function<T, R[]> f) {
            super(f.compose(ignored -> response.body()));
            checkNotNull(response);
            from(response);
        }
    }

    /**
     * Returns an array from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of response body
     * @param <R> is a type of item of resulted array
     */
    @SequentialGetStepSupplier.DefineTimeOutParameterName("Time to receive expected http response and get the result")
    @DefineGetImperativeParameterName(value = "Send http request. Wait for the response and then get:")
    public static final class GetObjectsFromArrayWhenResponseReceiving<T, R>
            extends GetObjectsFromArrayBodyStepSupplier<T, R, GetObjectsFromArrayWhenResponseReceiving<T, R>> {

        private final Function<T, R[]> f;

        private GetObjectsFromArrayWhenResponseReceiving(ResponseSequentialGetSupplierInternal<T> getResponse,
                                                         Function<T, R[]> f) {
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
        public GetObjectsFromArrayWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).timeOut(timeOut);
            return this;
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
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
        public GetObjectsFromArrayWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(description, predicate);
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromArrayWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(criteria);
            return this;
        }

        /**
         * Defines OR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromArrayWhenResponseReceiving<T, R> responseCriteriaOr(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaOr(criteria);
            return this;
        }

        /**
         * Defines XOR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromArrayWhenResponseReceiving<T, R> responseCriteriaOnlyOne(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaOnlyOne(criteria);
            return this;
        }

        /**
         * Defines NOT-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromArrayWhenResponseReceiving<T, R> responseCriteriaNot(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaNot(criteria);
            return this;
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> criteriaOr(Criteria<? super R>... criteria) {
            var orCriteria = OR(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(arrayBodyMatches(new BodyHasItems(orCriteria), f, orCriteria));
            return super.criteriaOr(criteria);
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> criteriaOnlyOne(Criteria<? super R>... criteria) {
            var xorCriteria = ONLY_ONE(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(arrayBodyMatches(new BodyHasItems(xorCriteria), f, xorCriteria));
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> criteriaNot(Criteria<? super R>... criteria) {
            var notCriteria = NOT(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(arrayBodyMatches(new BodyHasItems(notCriteria), f, notCriteria));
            return super.criteriaNot(criteria);
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> criteria(Criteria<? super R> criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(arrayBodyMatches(new BodyHasItems(criteria), f, criteria));
            return super.criteria(criteria);
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> criteria(String description, Predicate<? super R> criteria) {
            return criteria(condition(translate(description), criteria));
        }

        @Override
        public GetObjectsFromArrayWhenResponseReceiving<T, R> throwOnNoResult() {
            ((ResponseSequentialGetSupplierInternal<?>) getFrom()).throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}
