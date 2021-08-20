package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Response body has items: {description}")
@SuppressWarnings("unchecked")
final class ResponseExecutionResultHasItems<R> {
    @DescriptionFragment(value = "description")
    private final Criteria<R> criteria;

    ResponseExecutionResultHasItems(Criteria<? super R> criteria) {
        this.criteria = (Criteria<R>) criteria;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    public Criteria<R> getCriteria() {
        return criteria;
    }
}
