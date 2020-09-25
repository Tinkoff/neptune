package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.time.temporal.ChronoUnit;

@PropertyDescription(description = "Time unit (see java.time.temporal.ChronoUnit) of default time to connect to http service",
        section = "Http client. Connection")
@PropertyName("DEFAULT_HTTP_CONNECTION_TIME_UNIT")
@PropertyDefaultValue("SECONDS")
public final class DefaultConnectTimeOutUnitProperty implements EnumPropertySuppler<ChronoUnit> {

    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_CONNECTION_TIME_UNIT'} and converts it an instance
     * of {@link ChronoUnit}. This is the time unit of connection timeout.
     */
    public static final DefaultConnectTimeOutUnitProperty
            DEFAULT_CONNECT_TIME_UNIT_PROPERTY = new DefaultConnectTimeOutUnitProperty();

    private DefaultConnectTimeOutUnitProperty() {
        super();
    }
}
