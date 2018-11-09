package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

/**
 * This class is designed to read value of the property {@code 'waiting.for.desired.response.time.value'} and convert it to
 * long value. This is the value of the waiting for desired http response.
 */
public final class TimeValueToGetDesiredResponseProperty implements LongValuePropertySupplier {

    private static final String WAITING_FOR_DESIRED_RESPONSE_TIME_VALUE = "waiting.for.desired.response.time.value";

    /**
     * This instance reads value of the property {@code 'waiting.for.desired.response.time.value'} and converts it to
     * long value. This is the value of the waiting for desired http response.
     */
    public static final TimeValueToGetDesiredResponseProperty
            TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY = new TimeValueToGetDesiredResponseProperty();

    private TimeValueToGetDesiredResponseProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return WAITING_FOR_DESIRED_RESPONSE_TIME_VALUE;
    }
}
