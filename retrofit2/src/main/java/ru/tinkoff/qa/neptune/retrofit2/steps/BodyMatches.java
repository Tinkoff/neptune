package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Response body: {description}")
@SuppressWarnings("unchecked")
final class BodyMatches<R> {
    @DescriptionFragment(value = "description")
    final Criteria<R> criteria;

    private BodyMatches(Criteria<? super R> criteria) {
        this.criteria = (Criteria<R>) criteria;
    }

    static <R> BodyMatches<R> body(Criteria<? super R> criteria) {
        return new BodyMatches<>(criteria);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    Criteria<R> getCriteria() {
        return criteria;
    }
}
