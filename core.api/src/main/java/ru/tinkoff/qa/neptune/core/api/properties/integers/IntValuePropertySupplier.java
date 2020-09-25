package ru.tinkoff.qa.neptune.core.api.properties.integers;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return integer values.
 */
public interface IntValuePropertySupplier extends PropertySupplier<Integer> {

    @Override
    default Integer parse(String value) {
        return Integer.parseInt(value);
    }
}
