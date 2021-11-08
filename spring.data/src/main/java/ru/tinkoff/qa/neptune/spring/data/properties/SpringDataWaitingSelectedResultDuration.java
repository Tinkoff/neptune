package ru.tinkoff.qa.neptune.spring.data.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SpringDataSelectTimeUnitProperties.SPRING_DATA_SLEEPING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SpringDataSelectTimeUnitProperties.SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SpringDataSelectTimeValueProperties.SPRING_DATA_SLEEPING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.spring.data.properties.SpringDataWaitingSelectedResultDuration.SpringDataSelectTimeValueProperties.SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE;

public final class SpringDataWaitingSelectedResultDuration extends DurationSupplier {

    /**
     * Reads properties {@code 'SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT'} and
     * {@code 'SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE'} and builds an instance of {@link Duration}.
     * When any of properties is not defined then it builds a duration of 1 second.
     */
    public static final SpringDataWaitingSelectedResultDuration SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME =
            new SpringDataWaitingSelectedResultDuration(SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT,
                    SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE);

    /**
     * Reads properties {@code 'SPRING_DATA_SLEEPING_TIME_UNIT'} and {@code 'SPRING_DATA_SLEEPING_TIME_VALUE'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then it builds a duration of
     * 100 milliseconds.
     */
    public static final SpringDataWaitingSelectedResultDuration SPRING_DATA_SLEEPING_TIME =
            new SpringDataWaitingSelectedResultDuration(SPRING_DATA_SLEEPING_TIME_UNIT, SPRING_DATA_SLEEPING_TIME_VALUE);

    private SpringDataWaitingSelectedResultDuration(SpringDataSelectTimeUnitProperties springDataQueryTimeUnitProperties,
                                                    SpringDataSelectTimeValueProperties queryTimeValueProperties) {
        super(springDataQueryTimeUnitProperties, queryTimeValueProperties);
    }

    public enum SpringDataSelectTimeUnitProperties implements EnumPropertySuppler<ChronoUnit> {
        @PropertyDescription(description = {"Time unit (see java.time.temporal.ChronoUnit) of default time",
                "of the waiting for something is present in a repository"},
                section = "Data base. Getting the result. Waiting time")
        @PropertyName("SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT")
        @PropertyDefaultValue("SECONDS")
        SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_UNIT,

        @PropertyDescription(description = {"Time unit (see java.time.temporal.ChronoUnit) of default time",
                "of the sleeping between attempts to get something from a repository"},
                section = "Data base. Getting the result. Sleeping time")
        @PropertyName("SPRING_DATA_SLEEPING_TIME_UNIT")
        @PropertyDefaultValue("MILLIS")
        SPRING_DATA_SLEEPING_TIME_UNIT
    }

    public enum SpringDataSelectTimeValueProperties implements LongValuePropertySupplier {
        @PropertyDescription(description = "Default value of time of the waiting for something is present in a repository",
                section = "Data base. Getting the result. Waiting time")
        @PropertyName("SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE")
        @PropertyDefaultValue("1")
        SPRING_DATA_WAITING_FOR_SELECTION_RESULT_TIME_VALUE,

        @PropertyDescription(description = "Default value of time of the sleeping between attempts to get something from a repository",
                section = "Data base. Getting the result. Sleeping time")
        @PropertyName("SPRING_DATA_SLEEPING_TIME_VALUE")
        @PropertyDefaultValue("100")
        SPRING_DATA_SLEEPING_TIME_VALUE
    }
}
