package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

public class TestLongPropertySupplier implements LongValuePropertySupplier {

    static final String TEST_LONG_PROPERTY = "test.long.property";

    @Override
    public String getPropertyName() {
        return TEST_LONG_PROPERTY;
    }
}
