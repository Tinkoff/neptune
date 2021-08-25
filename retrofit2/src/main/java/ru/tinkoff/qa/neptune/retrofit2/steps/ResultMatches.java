package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Derived value from data of http-response body: {description}")
@SuppressWarnings("unchecked")
final class ResultMatches<R> {
    @DescriptionFragment(value = "description")
    final Criteria<R> criteria;

    private ResultMatches(Criteria<? super R> criteria) {
        this.criteria = (Criteria<R>) criteria;
    }

    static <R> ResultMatches<R> result(Criteria<? super R> criteria) {
        return new ResultMatches<>(criteria);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    Criteria<R> getCriteria() {
        return criteria;
    }
}
