package ru.tinkoff.qa.neptune.core.api.hamcrest.common;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("<{value}> doesn't match any of listed criteria")
public final class DoesNotMatchAnyCriteria extends MismatchDescriber {

    @DescriptionFragment("value")
    final Object value;

    public DoesNotMatchAnyCriteria(Object value) {
        this.value = value;
    }
}
