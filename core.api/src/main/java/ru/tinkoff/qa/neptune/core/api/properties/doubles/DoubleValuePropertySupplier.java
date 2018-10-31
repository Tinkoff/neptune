package ru.tinkoff.qa.neptune.core.api.properties.doubles;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return double values.
 */
public interface DoubleValuePropertySupplier extends PropertySupplier<Double> {
    @Override
    default Double get() {
        return returnOptionalFromEnvironment()
                .map(Double::parseDouble)
                .orElse(null);
    }
}
