package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("The object consists {actual} item(s). It is lesser that count of defined matchers ({expected})")
public final class LesserSizeMismatch extends MismatchDescriber {

    @DescriptionFragment("expected")
    final int expected;
    @DescriptionFragment("actual")
    final String actual;

    public LesserSizeMismatch(int expected, String actual) {
        this.expected = expected;
        this.actual = actual;
    }
}
