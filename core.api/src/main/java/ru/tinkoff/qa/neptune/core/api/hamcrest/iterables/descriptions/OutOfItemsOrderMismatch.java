package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("Actual order of items doesn't meet expectation. " +
        "The item [{current}; index: {currentIndex}; expected criteria: '{currentCriteria}'] " +
        "goes before the one: [{lastSuccessful}; index: {lastSuccessfulIndex}; expected criteria: '{lastSuccessfulCriteria}']")
public final class OutOfItemsOrderMismatch extends MismatchDescriber {

    @DescriptionFragment("current")
    final Object cuttent;

    @DescriptionFragment("currentIndex")
    final int currentIndex;

    @DescriptionFragment(value = "currentCriteria", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> current;

    @DescriptionFragment("lastSuccessful")
    final Object last;

    @DescriptionFragment("lastSuccessfulIndex")
    final int lastIndex;

    @DescriptionFragment(value = "lastSuccessfulCriteria", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> lastSuccessful;

    public OutOfItemsOrderMismatch(Object cuttent, int currentIndex, Matcher<?> current, Object last, int lastIndex, Matcher<?> lastSuccessful) {
        this.cuttent = cuttent;
        this.currentIndex = currentIndex;
        this.current = current;
        this.last = last;
        this.lastIndex = lastIndex;
        this.lastSuccessful = lastSuccessful;
    }
}
