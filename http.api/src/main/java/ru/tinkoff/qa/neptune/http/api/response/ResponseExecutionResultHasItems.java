package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Derived value from data of http-response body has items: {description}")
@SuppressWarnings("unchecked")
final class ResponseExecutionResultHasItems<R> {
    @DescriptionFragment(value = "description")
    private final Criteria<R> criteria;

    private ResponseExecutionResultHasItems(Criteria<? super R> criteria) {
        this.criteria = (Criteria<R>) criteria;
    }

    static <R> ResponseExecutionResultHasItems<R> hasResultItems(Criteria<? super R> criteria) {
        return new ResponseExecutionResultHasItems<>(criteria);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    public Criteria<R> getCriteria() {
        return criteria;
    }
}
