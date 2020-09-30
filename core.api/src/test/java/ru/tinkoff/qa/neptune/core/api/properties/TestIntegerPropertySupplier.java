package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.integers.IntValuePropertySupplier;

public class TestIntegerPropertySupplier implements IntValuePropertySupplier {

    static final String TEST_INTEGER_PROPERTY = "test.integer.property";

    @Override
    public String getName() {
        return TEST_INTEGER_PROPERTY;
    }
}
