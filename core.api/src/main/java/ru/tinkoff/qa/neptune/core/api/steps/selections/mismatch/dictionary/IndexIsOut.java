package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Index [{index}] is out of got items count [{count}]")
public final class IndexIsOut extends SelfDescribed {

    @DescriptionFragment("index")
    final int index;

    @DescriptionFragment("count")
    final int count;

    public IndexIsOut(int index, int count) {
        this.index = index;
        this.count = count;
    }
}
