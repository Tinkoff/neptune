package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Response body: {description}")
@SuppressWarnings("unchecked")
final class BodyMatches<R> extends SelfDescribed {
    @DescriptionFragment(value = "description")
    final Criteria<R> criteria;

    private BodyMatches(Criteria<? super R> criteria) {
        this.criteria = (Criteria<R>) criteria;
    }

    static <R> BodyMatches<R> body(Criteria<? super R> criteria) {
        return new BodyMatches<>(criteria);
    }

    Criteria<R> getCriteria() {
        return criteria;
    }
}
