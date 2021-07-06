package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("{actual} items instead of {expected}")
public final class DifferentSizeMismatch extends MismatchDescriber {

    @DescriptionFragment("expected")
    final int expected;
    @DescriptionFragment("actual")
    final int actual;

    public DifferentSizeMismatch(int expected, int actual) {
        this.expected = expected;
        this.actual = actual;
    }
}
