package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Derived value from data of http-response body has items: {description}")
@SuppressWarnings("unchecked")
final class ResultHasItems<R> {
    @DescriptionFragment(value = "description")
    final Criteria<R> criteria;

    private ResultHasItems(Criteria<? super R> criteria) {
        this.criteria = (Criteria<R>) criteria;
    }

    static <R> ResultHasItems<R> hasResultItems(Criteria<? super R> criteria) {
        return new ResultHasItems<>(criteria);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    Criteria<R> getCriteria() {
        return criteria;
    }
}
