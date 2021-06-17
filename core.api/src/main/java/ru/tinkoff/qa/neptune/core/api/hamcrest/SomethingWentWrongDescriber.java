package ru.tinkoff.qa.neptune.core.api.hamcrest;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

/**
 * It helps to form description of a mismatch when some {@link Throwable} is thrown
 * during the checking of an object.
 */
@Description("Something went wrong. The exception was thrown: {exception}")
final class SomethingWentWrongDescriber extends MismatchDescriber {

    @DescriptionFragment(value = "exception", makeReadableBy = ThrowableValueGetter.class)
    final Throwable thrown;

    SomethingWentWrongDescriber(Throwable thrown) {
        this.thrown = thrown;
    }
}
