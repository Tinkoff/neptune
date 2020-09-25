package ru.tinkoff.qa.neptune.core.api.properties.booleans;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return boolean values.
 */
public interface BooleanValuePropertySupplier extends PropertySupplier<Boolean> {

    @Override
    default Boolean parse(String value) {
        return Boolean.parseBoolean(value);
    }

    default Boolean returnIfNull() {
        return false;
    }
}
