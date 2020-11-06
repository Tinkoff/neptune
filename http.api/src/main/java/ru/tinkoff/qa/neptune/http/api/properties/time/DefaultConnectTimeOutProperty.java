package ru.tinkoff.qa.neptune.http.api.properties.time;

import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ru.tinkoff.qa.neptune.http.api.properties.time.DefaultConnectTimeOutUnitProperty.DEFAULT_CONNECT_TIME_UNIT_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.DefaultConnectTimeOutValueProperty.DEFAULT_CONNECT_TIME_VALUE_PROPERTY;

/**
 * This class is designed to read values of properties {@code 'DEFAULT_HTTP_CONNECTION_TIME_UNIT'} and
 * {@code 'DEFAULT_HTTP_CONNECTION_TIME'} convert it to {@link Duration}.
 */
public final class DefaultConnectTimeOutProperty extends DurationSupplier {

    /**
     * Reads properties {@code 'DEFAULT_HTTP_CONNECTION_TIME_UNIT'} and {@code 'DEFAULT_HTTP_CONNECTION_TIME'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then it builds a duration of
     * 5 seconds.
     */
    public static final DefaultConnectTimeOutProperty DEFAULT_CONNECT_TIME_OUT_PROPERTY =
            new DefaultConnectTimeOutProperty(DEFAULT_CONNECT_TIME_UNIT_PROPERTY,
                    DEFAULT_CONNECT_TIME_VALUE_PROPERTY);

    private DefaultConnectTimeOutProperty(EnumPropertySuppler<ChronoUnit> durationUnitPropertySupplier,
                                          LongValuePropertySupplier durationValuePropertySupplier) {
        super(durationUnitPropertySupplier, durationValuePropertySupplier);
    }
}
