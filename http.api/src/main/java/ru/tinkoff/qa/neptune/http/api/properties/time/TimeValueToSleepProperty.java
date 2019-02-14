package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

/**
 * This class is designed to read value of the property {@code 'waiting.for.response.sleep.time.value'} and convert it to
 * long value. This is the value of the sleeping time between attempts to get desired http response.
 */
public final class TimeValueToSleepProperty implements LongValuePropertySupplier {

    private static final String SLEEP_RESPONSE_TIME_VALUE = "waiting.for.response.sleep.time.value";

    /**
     * This instance reads value of the property {@code 'waiting.for.response.sleep.time.value'} and converts it to
     * long value. This is the value of the sleeping time between attempts to get desired http response.
     */
    public static final TimeValueToSleepProperty
            SLEEP_RESPONSE_TIME_VALUE_PROPERTY = new TimeValueToSleepProperty();

    private TimeValueToSleepProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return SLEEP_RESPONSE_TIME_VALUE;
    }
}
