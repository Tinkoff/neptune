package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

final class ResultCriteria {

    private ResultCriteria() {
        super();
    }

    static <M, T> Criteria<RequestExecutionResult<M, T>> bodyMatches(BodyMatches<M> b) {
        return condition(b.toString(), r -> b.getCriteria().get().test(r.getCallBody()));
    }

    static <M, T> Criteria<RequestExecutionResult<M, T>> resultResponseCriteria(Criteria<Response> criteria) {
        return condition(criteria.toString(), r -> criteria.get().test(r.getLastResponse()));
    }
}
