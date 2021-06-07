package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("No such http har header with name: {headerName}")
public final class NoSuchHarHeaderNameMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "headerName", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> matcher;

    public NoSuchHarHeaderNameMismatch(Matcher<?> matcher) {
        this.matcher = matcher;
    }
}
