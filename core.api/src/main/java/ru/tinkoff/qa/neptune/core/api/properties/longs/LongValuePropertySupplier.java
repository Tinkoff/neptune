package ru.tinkoff.qa.neptune.core.api.properties.longs;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return long values.
 */
public interface LongValuePropertySupplier extends PropertySupplier<Long> {
    @Override
    default Long get() {
        return returnOptionalFromEnvironment()
                .map(Long::parseLong)
                .orElse(null);
    }
}
