package ru.tinkoff.qa.neptune.core.api.properties.duration;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Duration.of;
import static java.util.Objects.nonNull;

public abstract class DurationSupplier implements Supplier<Duration> {

    private final EnumPropertySuppler<ChronoUnit> durationUnitPropertySupplier;
    private final LongValuePropertySupplier durationValuePropertySupplier;

    protected DurationSupplier(EnumPropertySuppler<ChronoUnit> durationUnitPropertySupplier,
                               LongValuePropertySupplier durationValuePropertySupplier) {
        checkArgument(nonNull(durationUnitPropertySupplier), "A supplier of time unit should be defined");
        checkArgument(nonNull(durationValuePropertySupplier), "A supplier of time value should be defined");
        this.durationUnitPropertySupplier = durationUnitPropertySupplier;
        this.durationValuePropertySupplier = durationValuePropertySupplier;
    }

    /**
     * This method creates supplied time unit value or returns default value when {@link EnumPropertySuppler#get()}
     * or {@link LongValuePropertySupplier#get()} return {@code null}
     *
     * @return built duration value.
     */
    public Duration get() {
        if (durationUnitPropertySupplier.get() == null || durationValuePropertySupplier.get() == null) {
            return null;
        }
        return of(durationValuePropertySupplier.get(), durationUnitPropertySupplier.get());
    }
}
