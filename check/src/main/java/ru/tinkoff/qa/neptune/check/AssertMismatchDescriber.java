package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Expected:{reason} {expectedDescription}\r\nChecked value: '{value}'\r\nResult: {actualDescription}")
final class AssertMismatchDescriber extends MismatchDescriber {

    @DescriptionFragment("reason")
    final String reason;

    @DescriptionFragment("expectedDescription")
    final String expectedDescription;

    @DescriptionFragment("value")
    final String value;

    @DescriptionFragment("actualDescription")
    final String actualDescription;

    AssertMismatchDescriber(String reason, String expectedDescription, String value, String actualDescription) {
        this.reason = reason;
        this.expectedDescription = expectedDescription;
        this.value = value;
        this.actualDescription = actualDescription;
    }
}
