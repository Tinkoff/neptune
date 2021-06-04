package ru.tinkoff.qa.neptune.core.api.hamcrest.pojo;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("returned value {description}")
final class ReturnedValueMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "description", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final org.hamcrest.Description description;

    ReturnedValueMismatch(org.hamcrest.Description description) {
        this.description = description;
    }
}
