package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Set of got items doesnt match '{condition}'")
public final class DoesntMatch {

    @DescriptionFragment("condition")
    final Criteria<?> condition;

    public DoesntMatch(Criteria<?> condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
