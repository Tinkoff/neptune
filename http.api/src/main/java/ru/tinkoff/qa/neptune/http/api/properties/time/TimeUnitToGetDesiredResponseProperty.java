package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.time.temporal.ChronoUnit;

/**
 * This class is designed to read value of the property {@code 'waiting.for.desired.response.time.unit'} and convert it to an instance
 * of {@link ChronoUnit}. This is the time unit of the waiting for desired http response.
 */
public final class TimeUnitToGetDesiredResponseProperty implements EnumPropertySuppler<ChronoUnit> {

    private static final String WAITING_FOR_DESIRED_RESPONSE_TIME_UNIT = "waiting.for.desired.response.time.unit";

    /**
     * This instance reads value of the property {@code 'waiting.for.desired.response.time.unit'} and converts it an instance
     * of {@link ChronoUnit}. This is the time unit of the waiting for desired http response.
     */
    public static final TimeUnitToGetDesiredResponseProperty
            TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY = new TimeUnitToGetDesiredResponseProperty();

    private TimeUnitToGetDesiredResponseProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return WAITING_FOR_DESIRED_RESPONSE_TIME_UNIT;
    }
}
