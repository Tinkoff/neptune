package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("No such column with header: {columnName}")
public final class NoSuchColumnMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "columnName", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> matcher;

    public NoSuchColumnMismatch(Matcher<?> matcher) {
        this.matcher = matcher;
    }
}
