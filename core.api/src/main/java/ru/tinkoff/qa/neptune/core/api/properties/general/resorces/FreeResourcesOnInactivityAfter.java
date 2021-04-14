package ru.tinkoff.qa.neptune.core.api.properties.general.resorces;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeUnit.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeValue.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE;

/**
 * Reads properties {@code 'TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT'} and
 * {@code 'TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE'} and builds an instance of {@link Duration}.
 * <p>
 * It have sense when value of th property {@code 'TO_FREE_RESOURCES_ON_INACTIVITY'} is {@code true}.
 */
public final class FreeResourcesOnInactivityAfter extends DurationSupplier {

    /**
     * Reads properties {@code 'TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT'} and
     * {@code 'TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE'} and builds an instance of {@link Duration}.
     * When any of properties is not defined then it builds a duration of 30 seconds.
     */
    public static final FreeResourcesOnInactivityAfter FREE_RESOURCES_ON_INACTIVITY_AFTER =
            new FreeResourcesOnInactivityAfter(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT,
                    FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE);

    private FreeResourcesOnInactivityAfter(FreeResourcesOnInactivityAfterTimeUnit freeResourcesTimeUnit,
                                           FreeResourcesOnInactivityAfterTimeValue freeResourcesValue) {
        super(freeResourcesTimeUnit, freeResourcesValue);
    }

    @PropertyDescription(description = {
            "Time unit (see java.time.temporal.ChronoUnit) of time to wait for activity",
            "It have sense when value of the property 'TO_FREE_RESOURCES_ON_INACTIVITY' is 'true'"},
            section = "General properties. Resource management")
    @PropertyName("TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT")
    @PropertyDefaultValue("SECONDS")
    public static class FreeResourcesOnInactivityAfterTimeUnit implements EnumPropertySuppler<ChronoUnit> {

        private FreeResourcesOnInactivityAfterTimeUnit() {
            super();
        }

        /**
         * Reads the property value and is used to get access to its value.
         */
        public static final FreeResourcesOnInactivityAfterTimeUnit FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT =
                new FreeResourcesOnInactivityAfterTimeUnit();
    }

    @PropertyDescription(description = {"Value of time to wait for activity",
            "It have sense when value of the property 'TO_FREE_RESOURCES_ON_INACTIVITY' is 'true'"},
            section = "General properties. Resource management")
    @PropertyName("TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE")
    @PropertyDefaultValue("30")
    public static class FreeResourcesOnInactivityAfterTimeValue implements LongValuePropertySupplier {

        private FreeResourcesOnInactivityAfterTimeValue() {
            super();
        }

        /**
         * Reads the property value and is used to get access to its value.
         */
        public static final FreeResourcesOnInactivityAfterTimeValue FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE =
                new FreeResourcesOnInactivityAfterTimeValue();
    }
}
