package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Table size {actualSize} is lesser than index if row {index}")
public final class LesserTableSizeMismatch extends MismatchDescriber {

    @DescriptionFragment("index")
    final int index;

    @DescriptionFragment("actualSize")
    final int actualSize;

    public LesserTableSizeMismatch(int index, int actualSize) {
        this.index = index;
        this.actualSize = actualSize;
    }
}
