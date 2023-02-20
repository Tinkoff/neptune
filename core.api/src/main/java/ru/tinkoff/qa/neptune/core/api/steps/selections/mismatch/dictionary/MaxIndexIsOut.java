package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Max index [{index}] is out of got items count [{count}]")
public final class MaxIndexIsOut extends SelfDescribed {

    @DescriptionFragment("index")
    final int minIndex;

    @DescriptionFragment("count")
    final int count;

    public MaxIndexIsOut(int minIndex, int count) {
        this.minIndex = minIndex;
        this.count = count;
    }
}
