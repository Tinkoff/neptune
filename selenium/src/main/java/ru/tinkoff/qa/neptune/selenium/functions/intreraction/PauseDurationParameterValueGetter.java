package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.time.Duration;

import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;

public final class PauseDurationParameterValueGetter implements ParameterValueGetter<Duration> {

    @Override
    public String getParameterValue(Duration fieldValue) {
        return formatDurationHMS(fieldValue.toMillis());
    }
}
