package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Count [{size}] of got items doesnt match '{condition}'")
public final class CountOfItemsDoesntMatchCondition {

    @DescriptionFragment("size")
    final int iterableSize;

    @DescriptionFragment("condition")
    final ItemsCountCondition condition;

    public CountOfItemsDoesntMatchCondition(int iterableSize, ItemsCountCondition condition) {
        this.iterableSize = iterableSize;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
