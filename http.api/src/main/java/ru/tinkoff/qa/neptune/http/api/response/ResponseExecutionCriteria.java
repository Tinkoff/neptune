package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

final class ResponseExecutionCriteria {

    private ResponseExecutionCriteria() {
        super();
    }

    private static <T, R> Criteria<ResponseExecutionResult<T, R>> executionResultMatches(String description,
                                                                                         Predicate<? super R> predicate) {
        checkArgument(nonNull(predicate), "Predicate should be defined");
        checkArgument(isNotBlank(description), "Description should not be defined as a blank or null string");
        return condition(description, r -> predicate.test(r.getResult()));
    }

    static <T, R> Criteria<ResponseExecutionResult<T, R>> executionResultMatches(ResponseExecutionResultMatches<R> b) {
        return executionResultMatches(b.toString(), b.getCriteria().get());
    }

    static <T, R, S extends Iterable<R>> Criteria<ResponseExecutionResult<T, S>> iterableResultMatches(ResponseExecutionResultHasItems<R> b) {
        return executionResultMatches(b.toString(), iterable -> stream(iterable.spliterator(), false)
                .anyMatch(b.getCriteria().get()));
    }

    static <T, R> Criteria<ResponseExecutionResult<T, R[]>> arrayResultMatches(ResponseExecutionResultHasItems<R> b) {
        return executionResultMatches(b.toString(), array -> Arrays.stream(array)
                .anyMatch(b.getCriteria().get()));
    }

    static <T, R> Criteria<ResponseExecutionResult<T, R>> responseResultMatches(Criteria<HttpResponse<T>> criteria) {
        return condition(criteria.toString(), r -> criteria.get().test(r.getResponse()));
    }

    static <T, R> Criteria<ResponseExecutionResult<T, R>> responseResultMatches(String description, Predicate<HttpResponse<T>> predicate) {
        return responseResultMatches(condition(description, predicate));
    }
}
