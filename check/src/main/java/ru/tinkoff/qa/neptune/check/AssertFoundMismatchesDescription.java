package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.check.parameter.value.getters.AssertionErrorsValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.Collection;

@Description("Found mismatches:\r\n{mismatches}")
final class AssertFoundMismatchesDescription extends MismatchDescriber {

    @DescriptionFragment(value = "mismatches", makeReadableBy = AssertionErrorsValueGetter.class)
    final Collection<AssertionError> assertionErrors;

    AssertFoundMismatchesDescription(Collection<AssertionError> assertionErrors) {
        this.assertionErrors = assertionErrors;
    }
}
