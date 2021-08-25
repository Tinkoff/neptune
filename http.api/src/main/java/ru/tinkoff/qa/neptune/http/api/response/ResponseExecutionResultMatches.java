package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Derived value from data of http-response body: {description}")
@SuppressWarnings("unchecked")
final class ResponseExecutionResultMatches<R> {
    @DescriptionFragment(value = "description")
    private final Criteria<R> criteria;

    private ResponseExecutionResultMatches(Criteria<? super R> criteria) {
        this.criteria = (Criteria<R>) criteria;
    }

    static <R> ResponseExecutionResultMatches<R> resultMatches(Criteria<? super R> criteria) {
        return new ResponseExecutionResultMatches<>(criteria);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    Criteria<R> getCriteria() {
        return criteria;
    }
}

