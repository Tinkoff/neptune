package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.floats.FloatValuePropertySupplier;

public class TestFloatPropertySupplier implements FloatValuePropertySupplier {

    static final String TEST_FLOAT_PROPERTY = "test.float.property";

    @Override
    public String getName() {
        return TEST_FLOAT_PROPERTY;
    }
}
