package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.waiting.time.DurationSupplier;
import ru.tinkoff.qa.neptune.http.api.properties.time.TimeUnitToGetDesiredResponseProperty;
import ru.tinkoff.qa.neptune.http.api.properties.time.TimeValueToGetDesiredResponseProperty;

import java.time.Duration;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeUnitToGetDesiredResponseProperty.TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeValueToGetDesiredResponseProperty.TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY;

/**
 * This class is designed to read values of properties {@code 'waiting.for.desired.response.time.unit'} and
 * {@code 'waiting.for.desired.response.time.value'} convert it to {@link Duration}. This is the duration of the
 * waiting for desired http response.
 */
public final class TimeToGetDesiredResponseProperty extends DurationSupplier {

    /**
     * Reads properties {@code 'waiting.for.desired.response.time.unit'} and {@code 'waiting.for.desired.response.time.value'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then in builds a duration of
     * 25 seconds. This is the duration of the waiting for desired http response.
     */
    public static final TimeToGetDesiredResponseProperty DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY =
            new TimeToGetDesiredResponseProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY,
                    TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY);

    private TimeToGetDesiredResponseProperty(TimeUnitToGetDesiredResponseProperty durationUnitPropertySupplier,
                                             TimeValueToGetDesiredResponseProperty durationValuePropertySupplier) {
        super(durationUnitPropertySupplier, durationValuePropertySupplier, ofSeconds(25));
    }
}
