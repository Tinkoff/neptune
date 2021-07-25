package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Response body has items: {description}")
final class BodyHasItems {
    @DescriptionFragment(value = "description")
    final String description;

    BodyHasItems(Criteria<?> criteria) {
        this.description = criteria.toString();
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
