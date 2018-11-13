package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

/**
 * This class is designed to read value of the property {@code 'default.http.connect.time.value'} and convert it to
 * long value. This is the value of connection timeout.
 */
public final class DefaultConnectTimeOutValueProperty implements LongValuePropertySupplier {

    private static final String DEFAULT_HTTP_CONNECT_TIME_VALUE = "default.http.connect.time.value";

    /**
     * This instance reads value of the property {@code 'default.http.connect.time.value'} and converts it to
     * long value. This is the value of connection timeout.
     */
    public static final DefaultConnectTimeOutValueProperty
            DEFAULT_CONNECT_TIME_VALUE_PROPERTY = new DefaultConnectTimeOutValueProperty();

    private DefaultConnectTimeOutValueProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return DEFAULT_HTTP_CONNECT_TIME_VALUE;
    }
}
