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
import static ru.tinkoff.qa.neptune.http.api.response.ResultCriteria.iterableBodyMatches;

/**
 * Builds a step-function that retrieves an object from some {@link Iterable} which is retrieved from
 * http response body.
 *
 * @param <T> is a type of response body
 * @param <R> is a type of resulted object
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public abstract class GetObjectFromIterableBodyStepSupplier<T, R, S extends GetObjectFromIterableBodyStepSupplier<T, R, S>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<HttpStepContext, R, HttpResponse<T>, S> {

    private <Q extends Iterable<R>> GetObjectFromIterableBodyStepSupplier(Function<T, Q> f) {
        super(((Function<HttpResponse<T>, T>) HttpResponse::body).andThen(f));
        addIgnored(Exception.class);
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of resulted object
     * @param <S>         if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived<T, R> asOneOfIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
                    String description,
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
     * @param <T>            is a type of response body
     * @param <R>            is a type of resulted object
     * @param <S>            if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    @Description("{description}")
    public static <T, R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving<T, R> asOneOfIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<T> handler,
            Function<T, S> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromIterableWhenResponseReceiving<>(responseInternal(requestBuilder, handler),
                f);
    }


    /**
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description is a description of resulted object
     * @param received    is a received http response
     * @param <R>         is a type of element of an iterable of response body
     * @param <S>         if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    public static <R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived<S, R> asOneOfIterable(
            String description,
            HttpResponse<S> received) {
        return asOneOfIterable(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceived}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of element of an iterable of response body
     * @param <S>            if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceived}
     */
    public static <R, S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving<S, R> asOneOfIterable(
            String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<S> handler) {
        return asOneOfIterable(description, requestBuilder, handler, rs -> rs);
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
    public static final class GetObjectFromIterableWhenResponseReceived<T, R>
            extends GetObjectFromIterableBodyStepSupplier<T, R, GetObjectFromIterableWhenResponseReceived<T, R>> {

        private <S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived(HttpResponse<T> response,
                                                                                  Function<T, S> f) {
            super(f.compose(ignored -> response.body()));
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
    public static final class GetObjectFromIterableWhenResponseReceiving<T, R>
            extends GetObjectFromIterableBodyStepSupplier<T, R, GetObjectFromIterableWhenResponseReceiving<T, R>> {

        private final Function<T, Iterable<R>> f;

        private <S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving(ResponseSequentialGetSupplierInternal<T> getResponse,
                                                                                   Function<T, S> f) {
            super(f);
            from(getResponse.addIgnored(Exception.class));
            this.f = (Function<T, Iterable<R>>) f;
        }

        /**
         * Defines time to receive a response and get desired data.
         *
         * @param timeOut is a time duration to receive a response and get desired data
         * @return self-reference
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).timeOut(timeOut);
            return this;
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
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
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(description, predicate);
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(criteria);
            return this;
        }

        /**
         * Defines OR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteriaOr(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaOr(criteria);
            return this;
        }

        /**
         * Defines XOR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteriaOnlyOne(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaOnlyOne(criteria);
            return this;
        }

        /**
         * Defines NOT-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteriaNot(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteriaNot(criteria);
            return this;
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteriaOr(Criteria<? super R>... criteria) {
            var orCriteria = OR(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(iterableBodyMatches(new BodyHasItems(orCriteria), f, orCriteria));
            return super.criteriaOr(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteriaOnlyOne(Criteria<? super R>... criteria) {
            var xorCriteria = ONLY_ONE(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(iterableBodyMatches(new BodyHasItems(xorCriteria), f, xorCriteria));
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteriaNot(Criteria<? super R>... criteria) {
            var notCriteria = NOT(criteria);
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(iterableBodyMatches(new BodyHasItems(notCriteria), f, notCriteria));
            return super.criteriaNot(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteria(Criteria<? super R> criteria) {
            ((ResponseSequentialGetSupplierInternal<T>) getFrom()).criteria(iterableBodyMatches(new BodyHasItems(criteria), f, criteria));
            return super.criteria(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteria(String description, Predicate<? super R> criteria) {
            return criteria(condition(translate(description), criteria));
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> throwOnNoResult() {
            ((ResponseSequentialGetSupplierInternal<?>) getFrom()).throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}
