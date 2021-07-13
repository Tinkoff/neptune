package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Response body has items: {description}")
final class BodyHasItems {
    @DescriptionFragment("description")
    final String description;

    BodyHasItems(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
