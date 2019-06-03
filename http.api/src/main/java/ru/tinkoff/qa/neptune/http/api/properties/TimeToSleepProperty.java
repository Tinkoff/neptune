package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.http.api.properties.time.TimeUnitToSleepProperty;
import ru.tinkoff.qa.neptune.http.api.properties.time.TimeValueToSleepProperty;

import java.time.Duration;

import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeUnitToSleepProperty.SLEEP_RESPONSE_TIME_UNIT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeValueToSleepProperty.SLEEP_RESPONSE_TIME_VALUE_PROPERTY;

/**
 * This class is designed to read values of properties {@code 'waiting.for.response.sleep.time.unit'} and
 * {@code 'waiting.for.response.sleep.time.value'} convert it to {@link Duration}. This is the duration of
 * the sleeping between attempts to get desired http response.
 */
public final class TimeToSleepProperty extends DurationSupplier {

    /**
     * Reads properties {@code 'waiting.for.response.sleep.time.unit'} and {@code 'waiting.for.response.sleep.time.value'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then it builds a duration of
     * 500 milliseconds. This is the duration of the sleeping between attempts to get desired http response.
     */
    public static final TimeToSleepProperty SLEEP_RESPONSE_TIME_PROPERTY =
            new TimeToSleepProperty(SLEEP_RESPONSE_TIME_UNIT_PROPERTY,
                    SLEEP_RESPONSE_TIME_VALUE_PROPERTY);

    private TimeToSleepProperty(TimeUnitToSleepProperty durationUnitPropertySupplier,
                                TimeValueToSleepProperty durationValuePropertySupplier) {
        super(durationUnitPropertySupplier, durationValuePropertySupplier, ofMillis(500));
    }
}
