package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("Key: {key}. Was expected: {expected}. Actually: {mismatch}.")
public class KeyMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "expected", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> expected;

    @DescriptionFragment("key")
    final Object key;

    @DescriptionFragment(value = "mismatch", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final org.hamcrest.Description mismatchDescription;

    public KeyMismatch(Matcher<?> expected, Object key, org.hamcrest.Description mismatchDescription) {
        this.expected = expected;
        this.key = key;
        this.mismatchDescription = mismatchDescription;
    }
}
