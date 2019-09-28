package ru.tinkoff.qa.neptune.core.api.properties.general.resorces;

import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.
        FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeUnit.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.
        FreeResourcesOnInactivityAfterTimeValue.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE;

/**
 * Reads properties {@code 'to.free.resources.on.inactivity.after.time.unit'} and
 * {@code 'to.free.resources.on.inactivity.after.time.value'} and builds an instance of {@link Duration}.
 *
 * It have sense when value of th property {@code 'to.free.resources.on.inactivity'} is {@code true}.
 */
public class FreeResourcesOnInactivityAfter extends DurationSupplier {

    /**
     * Reads properties {@code 'to.free.resources.on.inactivity.after.time.unit'} and
     * {@code 'to.free.resources.on.inactivity.after.time.value'} and builds an instance of {@link Duration}.
     * When any of properties is not defined then it builds a duration of 30 seconds.
     */
    public static final FreeResourcesOnInactivityAfter FREE_RESOURCES_ON_INACTIVITY_AFTER =
            new FreeResourcesOnInactivityAfter(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT,
                    FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE);

    private FreeResourcesOnInactivityAfter(FreeResourcesOnInactivityAfterTimeUnit freeResourcesTimeUnit,
                                           FreeResourcesOnInactivityAfterTimeValue freeResourcesValue) {
        super(freeResourcesTimeUnit, freeResourcesValue, ofSeconds(30));
    }

    /**
     * Reads the property {@code 'to.free.resources.on.inactivity.after.time.unit'}
     */
    public static class FreeResourcesOnInactivityAfterTimeUnit implements EnumPropertySuppler<ChronoUnit> {

        private FreeResourcesOnInactivityAfterTimeUnit() {
            super();
        }

        private static final String PROPERTY_NAME = "to.free.resources.on.inactivity.after.time.unit";

        /**
         * Reads the property value and is used to get access to its value.
         */
        public static final FreeResourcesOnInactivityAfterTimeUnit FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT =
                new FreeResourcesOnInactivityAfterTimeUnit();

        @Override
        public String getPropertyName() {
            return PROPERTY_NAME;
        }
    }

    /**
     * Reads the property {@code 'to.free.resources.on.inactivity.after.time.value'}
     */
    public static class FreeResourcesOnInactivityAfterTimeValue implements LongValuePropertySupplier {

        private FreeResourcesOnInactivityAfterTimeValue() {
            super();
        }

        private static final String PROPERTY_NAME = "to.free.resources.on.inactivity.after.time.value";

        /**
         * Reads the property value and is used to get access to its value.
         */
        public static final FreeResourcesOnInactivityAfterTimeValue FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE =
                new FreeResourcesOnInactivityAfterTimeValue();

        @Override
        public String getPropertyName() {
            return PROPERTY_NAME;
        }
    }
}
