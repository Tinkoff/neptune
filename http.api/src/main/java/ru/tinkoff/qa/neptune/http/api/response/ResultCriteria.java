package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class ResultCriteria {

    private ResultCriteria() {
        super();
    }

    private static <T, R> Criteria<HttpResponse<T>> bodyMatches(String description,
                                                                Function<T, R> f,
                                                                Predicate<? super R> predicate) {
        checkArgument(nonNull(predicate), "Predicate that checks response body should be defined");
        checkArgument(isNotBlank(description), "Description should not be defined as a blank or null string");
        return ResponseCriteria.bodyMatches(description, t -> predicate.test(f.apply(t)));
    }

    static <T, R> Criteria<HttpResponse<T>> bodyMatches(BodyMatches b, Function<T, R> f, Criteria<? super R> criteria) {
        return bodyMatches(b.toString(), f, criteria.get());
    }

    static <T, R, S extends Iterable<R>> Criteria<HttpResponse<T>> iterableBodyMatches(BodyHasItems b,
                                                                                       Function<T, S> f,
                                                                                       Criteria<? super R> criteria) {
        return bodyMatches(b.toString(),
                f,
                iterable -> stream(iterable.spliterator(), false).anyMatch(criteria.get()));
    }

    static <T, R> Criteria<HttpResponse<T>> arrayBodyMatches(BodyHasItems b,
                                                             Function<T, R[]> f,
                                                             Criteria<? super R> criteria) {
        return bodyMatches(b.toString(), f, array -> Arrays.stream(array)
                .anyMatch(criteria.get()));
    }
}
