package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("Actual items order doesn't meet expectation. The item '{current}' goes before the one '{lastSuccessful}'")
public final class OutOfItemsOrderMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "current", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> current;

    @DescriptionFragment(value = "lastSuccessful", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> lastSuccessful;

    public OutOfItemsOrderMismatch(Matcher<?> current, Matcher<?> lastSuccessful) {
        this.current = current;
        this.lastSuccessful = lastSuccessful;
    }
}
