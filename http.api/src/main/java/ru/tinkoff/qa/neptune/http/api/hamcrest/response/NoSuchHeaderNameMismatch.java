package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("No such http header with name: {headerName}")
final class NoSuchHeaderNameMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "headerName", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> matcher;

    NoSuchHeaderNameMismatch(Matcher<?> matcher) {
        this.matcher = matcher;
    }
}
