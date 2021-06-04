package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Has {count} such items")
public final class DifferentCountMismatch extends MismatchDescriber {

    @DescriptionFragment("count")
    final int count;

    public DifferentCountMismatch(int count) {
        this.count = count;
    }
}
