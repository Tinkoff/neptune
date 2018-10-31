package ru.tinkoff.qa.neptune.core.api.properties.floats;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return float values.
 */
public interface FloatValuePropertySupplier extends PropertySupplier<Float> {
    @Override
    default Float get() {
        return returnOptionalFromEnvironment()
                .map(Float::parseFloat)
                .orElse(null);
    }
}
