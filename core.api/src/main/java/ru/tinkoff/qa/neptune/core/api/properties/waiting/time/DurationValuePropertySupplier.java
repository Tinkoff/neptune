package ru.tinkoff.qa.neptune.core.api.properties.waiting.time;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

public interface DurationValuePropertySupplier extends PropertySupplier<Long> {
    @Override
    default Long get() {
        return returnOptionalFromEnvironment()
                .map(Long::parseLong)
                .orElse(null);
    }
}
