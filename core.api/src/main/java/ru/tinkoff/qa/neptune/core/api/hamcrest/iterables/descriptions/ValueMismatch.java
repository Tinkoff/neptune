package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("Value: {value}. {mismatch}.")
public class ValueMismatch extends MismatchDescriber {

    @DescriptionFragment("value")
    final Object key;

    @DescriptionFragment(value = "mismatch", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final org.hamcrest.Description mismatchDescription;

    public ValueMismatch(Object key, org.hamcrest.Description mismatchDescription) {
        this.key = key;
        this.mismatchDescription = mismatchDescription;
    }
}
