package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.Arrays;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

final class ResultCriteria {

    private ResultCriteria() {
        super();
    }

    @Description("{description}")
    private static <T> Criteria<RequestExecutionResult<T>> bodyMatches(
            @DescriptionFragment("description") String description,
            Predicate<? super T> predicate) {
        checkArgument(nonNull(predicate), "Predicate that checks response body should be defined");
        checkArgument(isNotBlank(description), "Description should not be defined as a blank or null string");
        return condition(r -> predicate.test(r.getResult()));
    }

    static <T> Criteria<RequestExecutionResult<T>> bodyMatches(BodyMatches b, Criteria<? super T> criteria) {
        return bodyMatches(b.toString(), criteria.get());
    }

    static <T, S extends Iterable<T>> Criteria<RequestExecutionResult<S>> iterableBodyMatches(BodyHasItems b,
                                                                                              Criteria<? super T> criteria) {
        return bodyMatches(b.toString(), iterable -> stream(iterable.spliterator(), false)
                .anyMatch(criteria.get()));
    }

    static <T> Criteria<RequestExecutionResult<T[]>> arrayBodyMatches(BodyHasItems b,
                                                                      Criteria<? super T> criteria) {
        return bodyMatches(b.toString(), array -> Arrays.stream(array)
                .anyMatch(criteria.get()));
    }

    static <T> Criteria<RequestExecutionResult<T>> resultResponseCriteria(Criteria<Response> criteria) {
        return condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse()));
    }
}
