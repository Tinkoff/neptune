package ru.tinkoff.qa.neptune.core.api.properties.waiting.time;

import java.time.Duration;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Duration.of;

public abstract class DurationSupplier implements Supplier<Duration> {

    private final DurationUnitPropertySupplier durationUnitPropertySupplier;
    private final DurationValuePropertySupplier durationValuePropertySupplier;

    protected DurationSupplier(DurationUnitPropertySupplier durationUnitPropertySupplier,
                               DurationValuePropertySupplier durationValuePropertySupplier) {
        checkArgument(durationUnitPropertySupplier != null, "A supplier of time unit should be defined");
        checkArgument(durationValuePropertySupplier != null, "A supplier of time value should be defined");
        this.durationUnitPropertySupplier = durationUnitPropertySupplier;
        this.durationValuePropertySupplier = durationValuePropertySupplier;
    }

    /**
     * This method creates supplied time unit value or returns given value when {@link DurationUnitPropertySupplier#get()}
     * or {@link DurationValuePropertySupplier#get()} return {@code null}
     *
     * @param returnWhenNotDefined a duration value that should be returned when {@link DurationUnitPropertySupplier#get()}
     *      or {@link DurationValuePropertySupplier#get()} return {@code null}
     * @return built duration value.
     */
    protected Duration getDuration(Duration returnWhenNotDefined) {
        if (durationUnitPropertySupplier.get() == null || durationValuePropertySupplier.get() == null) {
            return returnWhenNotDefined;
        }
        return of(durationValuePropertySupplier.get(), durationUnitPropertySupplier.get());
    }
}
