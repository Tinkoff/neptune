package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Max index [{index}] is out of got items count [{count}]")
public final class MaxIndexIsOut {

    @DescriptionFragment("index")
    final int minIndex;

    @DescriptionFragment("count")
    final int count;

    public MaxIndexIsOut(int minIndex, int count) {
        this.minIndex = minIndex;
        this.count = count;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
