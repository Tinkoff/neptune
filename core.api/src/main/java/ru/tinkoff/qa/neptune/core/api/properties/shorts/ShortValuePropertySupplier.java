package ru.tinkoff.qa.neptune.core.api.properties.shorts;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return short values.
 */
public interface ShortValuePropertySupplier extends PropertySupplier<Short> {
    @Override
    default Short parse(String value) {
        return Short.parseShort(value);
    }
}
