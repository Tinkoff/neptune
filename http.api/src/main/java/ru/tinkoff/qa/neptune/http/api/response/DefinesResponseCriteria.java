package ru.tinkoff.qa.neptune.http.api.response;

import com.google.common.collect.Streams;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.response.dictionary.AdditionalCriteriaDescription.hasResultItem;

@SuppressWarnings("unchecked")
public interface DefinesResponseCriteria<R, S extends DefinesResponseCriteria<R, S>> {

    static <T, R> Criteria<HttpResponse<T>> getResponseCriteriaForIterables(
        Object fromVal,
        Criteria<R> resultCriteria,
        String description
    ) {
        Criteria<HttpResponse<T>> responseCriteria = null;
        if (fromVal instanceof ResponseSequentialGetSupplier) {
            if (resultCriteria != null) {
                responseCriteria = condition(
                    hasResultItem(description, resultCriteria.toString()).toString(),
                    r -> ((Stream<R>) createStream(((Response<T, ?>) r).getCalculated())).anyMatch(resultCriteria.get())
                );
            } else {
                responseCriteria = condition(
                    hasResultItem(description).toString(),
                    r -> ((Stream<R>) createStream(((Response<T, ?>) r).getCalculated())).findAny().isPresent()
                );
            }
        }
        return responseCriteria;
    }

    private static <R> Stream<R> createStream(Object arrayOrIterable) {
        if (arrayOrIterable == null) {
            return null;
        }

        if (arrayOrIterable.getClass().isArray()) {
            return stream((R[]) arrayOrIterable);
        }

        return Streams.stream((Iterable<R>) arrayOrIterable);
    }

    private ResponseSequentialGetSupplier<R> getResponseStep() {
        try {
            var method = SequentialGetStepSupplier.class.getDeclaredMethod("getFrom");
            method.setAccessible(true);
            var result = method.invoke(this);
            if (result instanceof ResponseSequentialGetSupplier) {
                return (ResponseSequentialGetSupplier<R>) result;
            }

            return null;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Defines time to receive a response and get desired data.
     *
     * @param timeOut is a time duration to receive a response and get desired data
     * @return self-reference
     */
    default S retryTimeOut(Duration timeOut) {
        ofNullable(getResponseStep()).ifPresent(s -> s.timeOut(timeOut));
        return (S) this;
    }

    default S pollingInterval(Duration timeOut) {
        ofNullable(getResponseStep()).ifPresent(s -> s.pollingInterval(timeOut));
        return (S) this;
    }

    /**
     * Defines criteria for expected http response.
     *
     * @param description criteria description
     * @param predicate   is how to match http response
     * @return self-reference
     */
    default S responseCriteria(String description, Predicate<HttpResponse<R>> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    /**
     * Defines criteria for expected http response.
     *
     * @param criteria describes how to match http response
     * @return self-reference
     */
    default S responseCriteria(Criteria<HttpResponse<R>> criteria) {
        ofNullable(getResponseStep()).ifPresent(s -> s.criteria(criteria));
        return (S) this;
    }

    /**
     * Defines OR-expression for expected http response.
     *
     * @param criteria describes how to match http response
     * @return self-reference
     */
    default S responseCriteriaOr(Criteria<HttpResponse<R>>... criteria) {
        ofNullable(getResponseStep()).ifPresent(s -> s.criteriaOr(criteria));
        return (S) this;
    }

    /**
     * Defines XOR-expression for expected http response.
     *
     * @param criteria describes how to match http response
     * @return self-reference
     */
    default S responseCriteriaOnlyOne(Criteria<HttpResponse<R>>... criteria) {
        ofNullable(getResponseStep()).ifPresent(s -> s.criteriaOnlyOne(criteria));
        return (S) this;
    }

    /**
     * Defines NOT-expression for expected http response.
     *
     * @param criteria describes how to match http response
     * @return self-reference
     */
    default S responseCriteriaNot(Criteria<HttpResponse<R>>... criteria) {
        ofNullable(getResponseStep()).ifPresent(s -> s.criteriaNot(criteria));
        return (S) this;
    }

    default S throwOnNoResult() {
        ofNullable(getResponseStep()).ifPresent(SequentialGetStepSupplier::throwOnNoResult);
        return (S) this;
    }
}