package ru.tinkoff.qa.neptune.core.api.properties.floats;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return float values.
 */
public interface FloatValuePropertySupplier extends PropertySupplier<Float, Float> {

    @Override
    default Float parse(String value) {
        return Float.parseFloat(value);
    }
}
