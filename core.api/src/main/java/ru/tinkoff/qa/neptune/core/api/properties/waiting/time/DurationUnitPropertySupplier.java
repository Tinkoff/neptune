package ru.tinkoff.qa.neptune.core.api.properties.waiting.time;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.time.temporal.ChronoUnit;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public interface DurationUnitPropertySupplier extends PropertySupplier<ChronoUnit> {

    @Override
    default ChronoUnit get() {
        return returnOptionalFromEnvironment()
                .map(s -> stream(ChronoUnit.values())
                        .filter(timeUnit -> s.trim().equalsIgnoreCase(timeUnit.name()))
                        .findFirst()
                        .orElseThrow(
                                () -> new IllegalArgumentException(format("Property: %s. Unidentified " +
                                                "time unit %s. Please take a look at " +
                                                "elements of %s", this.toString(), s,
                                        ChronoUnit.class.getName())))).orElse(null);
    }
}
