package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.temporal.ChronoUnit;

import static java.time.Duration.ofMinutes;

public class TestDurationSupplier extends DurationSupplier {


    protected TestDurationSupplier(TestChronoUnitSupplier durationUnitPropertySupplier, TestTimeValueSupplier durationValuePropertySupplier) {
        super(durationUnitPropertySupplier, durationValuePropertySupplier, ofMinutes(1));
    }

    public static class TestChronoUnitSupplier implements EnumPropertySuppler<ChronoUnit> {

        static final String TEST_CHRONO_UNIT_PROPERTY = "test.chrono.unit.property";

        @Override
        public String getPropertyName() {
            return TEST_CHRONO_UNIT_PROPERTY;
        }
    }

    public static class TestTimeValueSupplier implements LongValuePropertySupplier {

        static final String TEST_TIME_VALUE_PROPERTY = "test.time.value.property";

        @Override
        public String getPropertyName() {
            return TEST_TIME_VALUE_PROPERTY;
        }
    }
}
