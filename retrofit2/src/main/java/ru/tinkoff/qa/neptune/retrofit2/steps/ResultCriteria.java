package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

final class ResultCriteria {

    private ResultCriteria() {
        super();
    }

    private static <M, T> Criteria<RequestExecutionResult<M, T>> resultMatches(
            @DescriptionFragment("description") String description,
            Predicate<? super T> predicate) {
        checkArgument(nonNull(predicate), "Predicate should be defined");
        checkArgument(isNotBlank(description), "Description should not be defined as a blank or null string");
        return condition(description, r -> predicate.test(r.getResult()));
    }

    static <M, T> Criteria<RequestExecutionResult<M, T>> bodyMatches(BodyMatches<M> b) {
        return condition(b.toString(), r -> b.getCriteria().get().test(r.getCallBody()));
    }

    static <M, T> Criteria<RequestExecutionResult<M, T>> resultResponseCriteria(Criteria<Response> criteria) {
        return condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse()));
    }
}
