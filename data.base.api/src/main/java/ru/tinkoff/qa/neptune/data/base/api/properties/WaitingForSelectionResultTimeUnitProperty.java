package ru.tinkoff.qa.neptune.data.base.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.waiting.time.DurationUnitPropertySupplier;

public final class WaitingForSelectionResultTimeUnitProperty implements DurationUnitPropertySupplier {

    private static final String WAITING_FOR_SELECTION_RESULT_UNIT = "waiting.db.selection.result.time.unit";

    /**
     * This instance reads value of the property {@code 'waiting.db.selection.result.time.unit'} and returns an
     * instance of {@link java.time.temporal.ChronoUnit}.
     */
    public static final WaitingForSelectionResultTimeUnitProperty
            DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_UNIT_PROPERTY = new WaitingForSelectionResultTimeUnitProperty();

    private WaitingForSelectionResultTimeUnitProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return WAITING_FOR_SELECTION_RESULT_UNIT;
    }
}
