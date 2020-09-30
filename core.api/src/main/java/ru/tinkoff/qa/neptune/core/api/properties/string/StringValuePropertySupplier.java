package ru.tinkoff.qa.neptune.core.api.properties.string;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interface is designed to read properties and return string values.
 */
public interface StringValuePropertySupplier extends PropertySupplier<String> {
    @Override
    default String parse(String value) {
        var trimmed = value.trim();
        if (isBlank(trimmed)) {
            return null;
        }
        return trimmed;
    }
}
