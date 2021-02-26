package ru.tinkoff.qa.neptune.core.api.properties.longs;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

/**
 * This interface is designed to read properties and return long values.
 */
public interface LongValuePropertySupplier extends PropertySupplier<Long, Long> {

    @Override
    default Long parse(String value) {
        return Long.parseLong(value);
    }
}
