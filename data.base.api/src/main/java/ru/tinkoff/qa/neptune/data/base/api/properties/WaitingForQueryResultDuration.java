package ru.tinkoff.qa.neptune.data.base.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeUnitProperties.SLEEPING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeUnitProperties.WAITING_FOR_SELECTION_RESULT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeValueProperties.SLEEPING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeValueProperties.WAITING_FOR_SELECTION_RESULT_TIME_VALUE;

public final class WaitingForQueryResultDuration extends DurationSupplier {

    /**
     * Reads properties {@code 'WAITING_DB_SELECTION_RESULT_TIME_UNIT'} and {@code 'WAITING_DB_SELECTION_RESULT_TIME_VALUE'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then it builds a duration of
     * 5 seconds.
     */
    public static final WaitingForQueryResultDuration WAITING_FOR_SELECTION_RESULT_TIME =
            new WaitingForQueryResultDuration(WAITING_FOR_SELECTION_RESULT_TIME_UNIT, WAITING_FOR_SELECTION_RESULT_TIME_VALUE);

    /**
     * Reads properties {@code 'SLEEPING_DB_SELECTION_RESULT_TIME_UNIT'} and {@code 'SLEEPING_DB_SELECTION_RESULT_TIME_VALUE'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then it builds a duration of
     * 100 milliseconds.
     */
    public static final WaitingForQueryResultDuration SLEEPING_TIME =
            new WaitingForQueryResultDuration(SLEEPING_TIME_UNIT, SLEEPING_TIME_VALUE);

    private WaitingForQueryResultDuration(QueryTimeUnitProperties queryTimeUnitProperties,
                                          QueryTimeValueProperties queryTimeValueProperties) {
        super(queryTimeUnitProperties, queryTimeValueProperties);
    }

    public enum QueryTimeUnitProperties implements EnumPropertySuppler<ChronoUnit> {
        @PropertyDescription(description = {"Time unit (see java.time.temporal.ChronoUnit) of default time",
                "of the waiting for something is present in a data store"},
                section = "Data base. Getting the result. Waiting time")
        @PropertyName("WAITING_DB_SELECTION_RESULT_TIME_UNIT")
        @PropertyDefaultValue("SECONDS")
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT,

        @PropertyDescription(description = {"Time unit (see java.time.temporal.ChronoUnit) of default time",
                "of the sleeping between attempts to get something from a data store"},
                section = "Data base. Getting the result. Sleeping time")
        @PropertyName("SLEEPING_DB_SELECTION_RESULT_TIME_UNIT")
        @PropertyDefaultValue("MILLIS")
        SLEEPING_TIME_UNIT
    }

    public enum QueryTimeValueProperties implements LongValuePropertySupplier {
        @PropertyDescription(description = "Default value of time of the waiting for something is present in a data store",
                section = "Data base. Getting the result. Waiting time")
        @PropertyName("WAITING_DB_SELECTION_RESULT_TIME_VALUE")
        @PropertyDefaultValue("5")
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE,

        @PropertyDescription(description = "Default value of time of the sleeping between attempts to get something from a data store",
                section = "Data base. Getting the result. Sleeping time")
        @PropertyName("SLEEPING_DB_SELECTION_RESULT_TIME_VALUE")
        @PropertyDefaultValue("100")
        SLEEPING_TIME_VALUE
    }
}
