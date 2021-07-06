package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("The item ['{currentCriteria}'] doesn't go after : [{lastSuccessful}; index: {lastSuccessfulIndex}; criteria: '{lastSuccessfulCriteria}']")
public final class OutOfItemsOrderMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "currentCriteria", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> current;

    @DescriptionFragment("lastSuccessful")
    final Object last;

    @DescriptionFragment("lastSuccessfulIndex")
    final int lastIndex;

    @DescriptionFragment(value = "lastSuccessfulCriteria", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> lastSuccessful;

    public OutOfItemsOrderMismatch(Matcher<?> current, Object last, int lastIndex, Matcher<?> lastSuccessful) {
        this.current = current;
        this.last = last;
        this.lastIndex = lastIndex;
        this.lastSuccessful = lastSuccessful;
    }
}
