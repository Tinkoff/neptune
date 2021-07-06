package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Failed to deserialize string: \r\n{toDeserialize}\r\n\r\nto object of type {type}")
public class DeserializationErrorMismatch extends MismatchDescriber {

    @DescriptionFragment("toDeserialize")
    final String toDeserialize;
    @DescriptionFragment("type")
    final Object type;

    public DeserializationErrorMismatch(String toDeserialize, Object type) {
        this.toDeserialize = toDeserialize;
        this.type = type;
    }
}
