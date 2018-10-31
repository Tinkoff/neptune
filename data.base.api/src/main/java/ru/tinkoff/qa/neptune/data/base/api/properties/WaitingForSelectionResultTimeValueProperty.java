package ru.tinkoff.qa.neptune.data.base.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

public final class WaitingForSelectionResultTimeValueProperty implements LongValuePropertySupplier {

    private static final String WAITING_FOR_SELECTION_RESULT_VALUE = "waiting.db.selection.result.time.value";

    /**
     * This instance reads value of the property {@code 'waiting.db.selection.result.time.value'}.
     */
    public static final WaitingForSelectionResultTimeValueProperty
            DEFAULT_WAITING_FOR_SELECTION_RESULT_TIME_VALUE_PROPERTY = new WaitingForSelectionResultTimeValueProperty();

    private WaitingForSelectionResultTimeValueProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return WAITING_FOR_SELECTION_RESULT_VALUE;
    }
}
