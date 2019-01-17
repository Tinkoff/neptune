package ru.tinkoff.qa.neptune.data.base.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;
import ru.tinkoff.qa.neptune.core.api.properties.waiting.time.DurationSupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeUnitProperties.SLEEPING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeUnitProperties.WAITING_FOR_SELECTION_RESULT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeValueProperties.SLEEPING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.QueryTimeValueProperties.WAITING_FOR_SELECTION_RESULT_TIME_VALUE;

public final class WaitingForQueryResultDuration extends DurationSupplier {

    /**
     * Reads properties {@code 'waiting.db.selection.result.time.unit'} and {@code 'waiting.db.selection.result.time.value'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then in builds a duration of
     * 5 seconds.
     */
    public static final WaitingForQueryResultDuration WAITING_FOR_SELECTION_RESULT_TIME =
            new WaitingForQueryResultDuration(WAITING_FOR_SELECTION_RESULT_TIME_UNIT,
                    WAITING_FOR_SELECTION_RESULT_TIME_VALUE, ofSeconds(5));

    /**
     * Reads properties {@code 'sleeping.db.selection.time.unit'} and {@code 'sleeping.db.selection.time.value'}
     * and builds an instance of {@link Duration}. When any of properties is not defined then in builds a duration of
     * 100 milliseconds.
     */
    public static final WaitingForQueryResultDuration SLEEPING_TIME =
            new WaitingForQueryResultDuration(SLEEPING_TIME_UNIT,
                    SLEEPING_TIME_VALUE, ofMillis(100));

    private WaitingForQueryResultDuration(QueryTimeUnitProperties queryTimeUnitProperties,
                                          QueryTimeValueProperties queryTimeValueProperties,
                                          Duration returnWhenNotDefined) {
        super(queryTimeUnitProperties, queryTimeValueProperties, returnWhenNotDefined);
    }

    public enum QueryTimeUnitProperties implements EnumPropertySuppler<ChronoUnit> {
        WAITING_FOR_SELECTION_RESULT_TIME_UNIT("waiting.db.selection.result.time.unit"),
        SLEEPING_TIME_UNIT("sleeping.db.selection.time.unit");

        private final String propertyName;

        QueryTimeUnitProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }
    }

    public enum QueryTimeValueProperties implements LongValuePropertySupplier {
        WAITING_FOR_SELECTION_RESULT_TIME_VALUE("waiting.db.selection.result.time.value"),
        SLEEPING_TIME_VALUE("sleeping.db.selection.time.value");

        private final String propertyName;

        QueryTimeValueProperties(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }
    }
}
