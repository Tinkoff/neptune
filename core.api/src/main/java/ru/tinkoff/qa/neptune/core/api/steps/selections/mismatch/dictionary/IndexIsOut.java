package ru.tinkoff.qa.neptune.core.api.steps.selections.mismatch.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Index [{index}] is out of found items count [{count}]")
public final class IndexIsOut {

    @DescriptionFragment("index")
    final int index;

    @DescriptionFragment("count")
    final int count;

    public IndexIsOut(int index, int count) {
        this.index = index;
        this.count = count;
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
