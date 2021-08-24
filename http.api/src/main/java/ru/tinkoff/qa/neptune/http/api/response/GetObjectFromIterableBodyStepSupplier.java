package ru.tinkoff.qa.neptune.http.api.response;

import com.google.common.collect.Iterables;
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
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.iterableResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.responseResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionResultHasItems.hasResultItems;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplierInternal.responseInternal;

/**
 * Builds a step-function that retrieves an object from some {@link Iterable} which is retrieved from
 * http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of a resulted value")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public abstract class GetObjectFromIterableBodyStepSupplier<T, R, M, S extends GetObjectFromIterableBodyStepSupplier<T, R, M, S>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<HttpStepContext, R, M, S> {

    private <Q extends Iterable<R>> GetObjectFromIterableBodyStepSupplier(Function<M, Q> f) {
        super(f);
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
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceiving}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param f              is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>            is a type of response body
     * @param <R>            is a type of resulted object
     * @param <S>            if a type of {@link Iterable} of R
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceiving}
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
        return new GetObjectFromIterableWhenResponseReceiving<>(responseInternal(translate(description),
                requestBuilder,
                handler,
                f,
                rs -> Iterables.size(rs) > 0));
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
     * Creates an instance of {@link GetObjectFromIterableWhenResponseReceiving}. It builds a step-function that retrieves an object from some
     * {@link Iterable} which is retrieved from http response body.
     *
     * @param description    is a description of resulted object
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of element of an iterable of response body
     * @param <S>            if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectFromIterableWhenResponseReceiving}
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
    @DefineFromParameterName("Response")
    public static final class GetObjectFromIterableWhenResponseReceived<T, R>
            extends GetObjectFromIterableBodyStepSupplier<T, R, HttpResponse<T>, GetObjectFromIterableWhenResponseReceived<T, R>> {

        private <S extends Iterable<R>> GetObjectFromIterableWhenResponseReceived(HttpResponse<T> response, Function<T, S> f) {
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
    public static final class GetObjectFromIterableWhenResponseReceiving<T, R>
            extends GetObjectFromIterableBodyStepSupplier<T, R, ResponseExecutionResult<T, ? extends Iterable<R>>, GetObjectFromIterableWhenResponseReceiving<T, R>> {

        private Criteria<R> derivedValueCriteria;

        private <S extends Iterable<R>> GetObjectFromIterableWhenResponseReceiving(ResponseSequentialGetSupplierInternal<T, S> getResponse) {
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
        public GetObjectFromIterableWhenResponseReceiving<T, R> retryTimeOut(Duration timeOut) {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).timeOut(timeOut);
            return this;
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> pollingInterval(Duration pollingTime) {
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
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
            return responseCriteria(condition(description, predicate));
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            ((ResponseSequentialGetSupplierInternal<T, Iterable<R>>) getFrom()).criteria(responseResultMatches(criteria));
            return this;
        }

        /**
         * Defines OR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteriaOr(Criteria<HttpResponse<T>>... criteria) {
            return responseCriteria(OR(criteria));
        }

        /**
         * Defines XOR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteriaOnlyOne(Criteria<HttpResponse<T>>... criteria) {
            return responseCriteria(ONLY_ONE(criteria));
        }

        /**
         * Defines NOT-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectFromIterableWhenResponseReceiving<T, R> responseCriteriaNot(Criteria<HttpResponse<T>>... criteria) {
            return responseCriteria(NOT(criteria));
        }

        private void criteriaForDerivedValue(Criteria<? super R> criteria) {
            derivedValueCriteria = ofNullable(derivedValueCriteria)
                    .map(c -> AND(c, criteria))
                    .orElse((Criteria<R>) criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteriaOr(Criteria<? super R>... criteria) {
            criteriaForDerivedValue(OR(criteria));
            return super.criteriaOr(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteriaOnlyOne(Criteria<? super R>... criteria) {
            criteriaForDerivedValue(ONLY_ONE(criteria));
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteriaNot(Criteria<? super R>... criteria) {
            criteriaForDerivedValue(NOT(criteria));
            return super.criteriaNot(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteria(Criteria<? super R> criteria) {
            criteriaForDerivedValue(criteria);
            return super.criteria(criteria);
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> criteria(String description, Predicate<? super R> criteria) {
            return criteria(condition(description, criteria));
        }

        @Override
        public Function<HttpStepContext, R> get() {
            if (derivedValueCriteria != null) {
                ((ResponseSequentialGetSupplierInternal<T, Iterable<R>>) getFrom())
                        .criteria(iterableResultMatches(hasResultItems(derivedValueCriteria)));
            }
            return super.get();
        }

        @Override
        public GetObjectFromIterableWhenResponseReceiving<T, R> throwOnNoResult() {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}
