package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;

public class TestBooleanPropertySupplier implements BooleanValuePropertySupplier {

    static final String TEST_BOOLEAN_PROPERTY = "test.boolean.property";

    @Override
    public String getName() {
        return TEST_BOOLEAN_PROPERTY;
    }
}
