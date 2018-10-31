package ru.tinkoff.qa.neptune.core.api.properties.waiting.time;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Duration.of;

public abstract class DurationSupplier implements Supplier<Duration> {

    private final EnumPropertySuppler<ChronoUnit> durationUnitPropertySupplier;
    private final LongValuePropertySupplier durationValuePropertySupplier;
    private final Duration returnWhenNotDefined;

    protected DurationSupplier(EnumPropertySuppler<ChronoUnit> durationUnitPropertySupplier,
                               LongValuePropertySupplier durationValuePropertySupplier, Duration returnWhenNotDefined) {
        checkArgument(returnWhenNotDefined != null, "Default duration value should be defined");
        checkArgument(durationUnitPropertySupplier != null, "A supplier of time unit should be defined");
        checkArgument(durationValuePropertySupplier != null, "A supplier of time value should be defined");
        this.returnWhenNotDefined = returnWhenNotDefined;
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
            return returnWhenNotDefined;
        }
        return of(durationValuePropertySupplier.get(), durationUnitPropertySupplier.get());
    }
}
