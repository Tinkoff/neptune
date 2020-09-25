package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

@PropertyDescription(description = "Value of default time to connect to http service",
        section = "Http client. Connection")
@PropertyName("DEFAULT_HTTP_CONNECTION_TIME")
@PropertyDefaultValue("5")
public final class DefaultConnectTimeOutValueProperty implements LongValuePropertySupplier {
    /**
     * This instance reads value of the property {@code 'DEFAULT_HTTP_CONNECTION_TIME'} and converts it to
     * long value. This is the value of connection timeout.
     */
    public static final DefaultConnectTimeOutValueProperty
            DEFAULT_CONNECT_TIME_VALUE_PROPERTY = new DefaultConnectTimeOutValueProperty();

    private DefaultConnectTimeOutValueProperty() {
        super();
    }
}
