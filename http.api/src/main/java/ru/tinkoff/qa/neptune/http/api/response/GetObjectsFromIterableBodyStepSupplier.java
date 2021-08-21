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
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.iterableResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseExecutionCriteria.responseResultMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplierInternal.responseInternal;

/**
 * Builds a step-function that retrieves an {@link Iterable} from http response body.
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
@ThrowWhenNoData(toThrow = DesiredDataHasNotBeenReceivedException.class, startDescription = "No data received:")
@SuppressWarnings("unchecked")
public abstract class GetObjectsFromIterableBodyStepSupplier<T, R, S extends Iterable<R>, M, Q extends GetObjectsFromIterableBodyStepSupplier<T, R, S, M, Q>>
        extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<HttpStepContext, S, M, R, Q> {

    private GetObjectsFromIterableBodyStepSupplier(Function<M, S> f) {
        super(f);
        addIgnored(Exception.class);
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableWhenResponseReceived}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description is a description of resulted {@link Iterable}
     * @param received    is a received http response
     * @param f           is a function that describes how to get an {@link Iterable} from a body of http response
     * @param <T>         is a type of response body
     * @param <R>         is a type of item of resulted iterable
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
     * @param <T>            is a type of response body
     * @param <R>            is a type of item of resulted iterable
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
        return new GetObjectsFromIterableWhenResponseReceiving<>(responseInternal(translate(description),
                requestBuilder,
                handler,
                f, rs -> Iterables.size(rs) > 0));
    }


    /**
     * Creates an instance of {@link GetObjectsFromIterableWhenResponseReceived}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description is a description of resulted {@link Iterable}
     * @param received    is a received http response
     * @param <R>         is a type of element of an iterable of response body
     * @param <S>         if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectsFromIterableWhenResponseReceived}
     */
    public static <R, S extends Iterable<R>> GetObjectsFromIterableWhenResponseReceived<S, R, S> asIterable(
            String description,
            HttpResponse<S> received) {
        return asIterable(description, received, rs -> rs);
    }

    /**
     * Creates an instance of {@link GetObjectsFromIterableWhenResponseReceiving}. It builds a step-function that retrieves
     * an {@link Iterable} from http response body.
     *
     * @param description    is a description of resulted {@link Iterable}
     * @param requestBuilder describes a request to be sent
     * @param handler        is a response body handler
     * @param <R>            is a type of element of an iterable of response body
     * @param <S>            if a type of {@link Iterable} of response body
     * @return an instance of {@link GetObjectsFromIterableWhenResponseReceiving}
     */
    public static <R, S extends Iterable<R>> GetObjectsFromIterableWhenResponseReceiving<S, R, S> asIterable(
            String description,
            RequestBuilder requestBuilder,
            HttpResponse.BodyHandler<S> handler) {
        return asIterable(description, requestBuilder, handler, rs -> rs);
    }

    /**
     * Returns an {@link Iterable} from a body of http response which is received already.
     *
     * @param <T> is a type of response body
     * @param <R> is a type of element of resulted iterable
     * @param <S> is a type of resulted iterable
     */
    @SuppressWarnings("unused")
    @DefineGetImperativeParameterName(value = "From http response get:")
    @DefineFromParameterName("Response")
    public static final class GetObjectsFromIterableWhenResponseReceived<T, R, S extends Iterable<R>>
            extends GetObjectsFromIterableBodyStepSupplier<T, R, S, HttpResponse<T>, GetObjectsFromIterableWhenResponseReceived<T, R, S>> {

        private GetObjectsFromIterableWhenResponseReceived(HttpResponse<T> response, Function<T, S> f) {
            super(f.compose(HttpResponse::body));
            checkNotNull(response);
            from(response);
        }
    }

    /**
     * Returns an {@link Iterable} from a body of http response. Response is supposed to be received during the step execution
     *
     * @param <T> is a type of response body
     * @param <R> is a type of element of resulted iterable
     * @param <S> is a type of resulted iterable
     */
    public static final class GetObjectsFromIterableWhenResponseReceiving<T, R, S extends Iterable<R>>
            extends GetObjectsFromIterableBodyStepSupplier<T, R, S, ResponseExecutionResult<T, S>, GetObjectsFromIterableWhenResponseReceiving<T, R, S>> {

        private GetObjectsFromIterableWhenResponseReceiving(ResponseSequentialGetSupplierInternal<T, S> getResponse) {
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
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> retryTimeOut(Duration timeOut) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).timeOut(timeOut);
            return this;
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> pollingInterval(Duration pollingTime) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).pollingInterval(pollingTime);
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param description criteria description
         * @param predicate   is how to match http response
         * @return self-reference
         */
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> responseCriteria(String description, Predicate<HttpResponse<T>> predicate) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(responseResultMatches(description, predicate));
            return this;
        }

        /**
         * Defines criteria for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> responseCriteria(Criteria<HttpResponse<T>> criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(responseResultMatches(criteria));
            return this;
        }

        /**
         * Defines OR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> responseCriteriaOr(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(responseResultMatches(OR(criteria)));
            return this;
        }

        /**
         * Defines XOR-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> responseCriteriaOnlyOne(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(responseResultMatches(ONLY_ONE(criteria)));
            return this;
        }

        /**
         * Defines NOT-expression for expected http response.
         *
         * @param criteria describes how to match http response
         * @return self-reference
         */
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> responseCriteriaNot(Criteria<HttpResponse<T>>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(responseResultMatches(NOT(criteria)));
            return this;
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> criteriaOr(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(iterableResultMatches((new ResponseExecutionResultHasItems<>(OR(criteria)))));
            return super.criteriaOr(criteria);
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> criteriaOnlyOne(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(iterableResultMatches(new ResponseExecutionResultHasItems<>(ONLY_ONE(criteria))));
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> criteriaNot(Criteria<? super R>... criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(iterableResultMatches(new ResponseExecutionResultHasItems<>(NOT(criteria))));
            return super.criteriaNot(criteria);
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> criteria(Criteria<? super R> criteria) {
            ((ResponseSequentialGetSupplierInternal<T, S>) getFrom()).criteria(iterableResultMatches(new ResponseExecutionResultHasItems<>(criteria)));
            return super.criteria(criteria);
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> criteria(String description, Predicate<? super R> criteria) {
            return criteria(condition(translate(description), criteria));
        }

        @Override
        public GetObjectsFromIterableWhenResponseReceiving<T, R, S> throwOnNoResult() {
            ((ResponseSequentialGetSupplierInternal<?, ?>) getFrom()).throwOnNoResult();
            return super.throwOnNoResult();
        }
    }
}

