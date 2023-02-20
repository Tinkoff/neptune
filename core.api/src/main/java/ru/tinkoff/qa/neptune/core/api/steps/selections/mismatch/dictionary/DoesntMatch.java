package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Set of got items doesnt match '{condition}'")
public final class DoesntMatch extends SelfDescribed {

    @DescriptionFragment("condition")
    final Criteria<?> condition;

    public DoesntMatch(Criteria<?> condition) {
        this.condition = condition;
    }
}
