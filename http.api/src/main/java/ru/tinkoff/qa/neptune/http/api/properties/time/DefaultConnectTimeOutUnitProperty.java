package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.time.temporal.ChronoUnit;

/**
 * This class is designed to read value of the property {@code 'default.http.connect.time.unit'} and convert it to an instance
 * of {@link ChronoUnit}. This is the time unit of connection timeout.
 */
public final class DefaultConnectTimeOutUnitProperty implements EnumPropertySuppler<ChronoUnit> {

    private static final String DEFAULT_HTTP_CONNECT_TIME_UNIT = "default.http.connect.time.unit";

    /**
     * This instance reads value of the property {@code 'default.http.connect.time.unit'} and converts it an instance
     * of {@link ChronoUnit}. This is the time unit of connection timeout.
     */
    public static final DefaultConnectTimeOutUnitProperty
            DEFAULT_CONNECT_TIME_UNIT_PROPERTY = new DefaultConnectTimeOutUnitProperty();

    private DefaultConnectTimeOutUnitProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return DEFAULT_HTTP_CONNECT_TIME_UNIT;
    }
}
