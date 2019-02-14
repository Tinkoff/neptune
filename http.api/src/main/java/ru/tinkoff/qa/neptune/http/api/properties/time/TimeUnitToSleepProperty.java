package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.time.temporal.ChronoUnit;

/**
 * This class is designed to read value of the property {@code 'waiting.for.response.sleep.time.unit'} and convert it to an instance
 * of {@link ChronoUnit}. This is the time unit of the sleeping between attempts to get desired http response.
 */
public final class TimeUnitToSleepProperty implements EnumPropertySuppler<ChronoUnit> {

    private static final String SLEEP_RESPONSE_TIME_UNIT = "waiting.for.response.sleep.time.unit";

    /**
     * This instance reads value of the property {@code 'waiting.for.response.sleep.time.unit'} and converts it an instance
     * of {@link ChronoUnit}. This is the time unit of the sleeping between attempts to get desired http response.
     */
    public static final TimeUnitToSleepProperty
            SLEEP_RESPONSE_TIME_UNIT_PROPERTY = new TimeUnitToSleepProperty();

    private TimeUnitToSleepProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return SLEEP_RESPONSE_TIME_UNIT;
    }
}
