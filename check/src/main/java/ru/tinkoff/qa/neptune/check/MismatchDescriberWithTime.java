package ru.tinkoff.qa.neptune.check;


import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.time.Duration;

@Description("{otherDescriber}. Time of the waiting for the matching: {duration}")
final class MismatchDescriberWithTime extends MismatchDescriber {

    @DescriptionFragment(value = "duration", makeReadableBy = ParameterValueGetter.DurationParameterValueGetter.class)
    final Duration duration;

    @DescriptionFragment("otherDescriber")
    final String otherDescription;

    MismatchDescriberWithTime(Duration duration, String otherDescription) {
        this.duration = duration;
        this.otherDescription = otherDescription;
    }
}
