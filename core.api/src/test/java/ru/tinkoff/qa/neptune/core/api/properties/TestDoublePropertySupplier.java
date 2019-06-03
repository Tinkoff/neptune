package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.doubles.DoubleValuePropertySupplier;

public class TestDoublePropertySupplier implements DoubleValuePropertySupplier {

    static final String TEST_DOUBLE_PROPERTY = "test.double.property";

    @Override
    public String getPropertyName() {
        return TEST_DOUBLE_PROPERTY;
    }
}
