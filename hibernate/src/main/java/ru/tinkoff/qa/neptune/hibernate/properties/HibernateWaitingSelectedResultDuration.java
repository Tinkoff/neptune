package ru.tinkoff.qa.neptune.hibernate.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HibernateSelectTimeUnitProperties.HIBERNATE_SLEEPING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HibernateSelectTimeUnitProperties.HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HibernateSelectTimeValueProperties.HIBERNATE_SLEEPING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.hibernate.properties.HibernateWaitingSelectedResultDuration.HibernateSelectTimeValueProperties.HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE;

public final class HibernateWaitingSelectedResultDuration extends DurationSupplier {

    /**
     * Reads properties {@code 'HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT'} and
     * {@code 'HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE'} and builds an instance of {@link Duration}.
     * When any of properties is not defined it builds a 1-second duration.
     */
    public static final HibernateWaitingSelectedResultDuration HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME =
            new HibernateWaitingSelectedResultDuration(HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT,
                    HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE);

    /**
     * Reads properties {@code 'HIBERNATE_SLEEPING_TIME_UNIT'} and {@code 'HIBERNATE_SLEEPING_TIME_VALUE'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then it builds a
     * 100-milliseconds duration.
     */
    public static final HibernateWaitingSelectedResultDuration HIBERNATE_SLEEPING_TIME =
            new HibernateWaitingSelectedResultDuration(HIBERNATE_SLEEPING_TIME_UNIT,
                    HIBERNATE_SLEEPING_TIME_VALUE);

    private HibernateWaitingSelectedResultDuration(HibernateSelectTimeUnitProperties hibernateSelectTimeUnitProperties,
                                                   HibernateSelectTimeValueProperties hibernateSelectTimeValueProperties) {
        super(hibernateSelectTimeUnitProperties, hibernateSelectTimeValueProperties);
    }

    public enum HibernateSelectTimeUnitProperties implements EnumPropertySuppler<ChronoUnit> {

        @PropertyDescription(description = {"Default time unit (see java.time.temporal.ChronoUnit)",
                "of waiting for something is present in database"},
                section = "Database. Getting the result. Waiting time")
        @PropertyName("HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT")
        @PropertyDefaultValue("SECONDS")
        HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_UNIT,

        @PropertyDescription(description = {"Default time unit (see java.time.temporal.ChronoUnit)",
                "of sleeping between attempts to get something from database"},
                section = "Database. Getting the result. Sleeping time")
        @PropertyName("HIBERNATE_SLEEPING_TIME_UNIT")
        @PropertyDefaultValue("MILLIS")
        HIBERNATE_SLEEPING_TIME_UNIT
    }

    public enum HibernateSelectTimeValueProperties implements LongValuePropertySupplier {

        @PropertyDescription(description = "Default time value of waiting for something is present in database",
                section = "Database. Getting the result. Waiting time")
        @PropertyName("HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE")
        @PropertyDefaultValue("1")
        HIBERNATE_WAITING_FOR_SELECTION_RESULT_TIME_VALUE,

        @PropertyDescription(description = "Default time value of sleeping between attempts to get something from database",
                section = "Database. Getting the result. Sleeping time")
        @PropertyName("HIBERNATE_SLEEPING_TIME_VALUE")
        @PropertyDefaultValue("100")
        HIBERNATE_SLEEPING_TIME_VALUE
    }
}
