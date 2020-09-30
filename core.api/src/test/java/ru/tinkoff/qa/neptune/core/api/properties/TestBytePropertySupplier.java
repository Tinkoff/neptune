package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.bytes.ByteValuePropertySupplier;

public class TestBytePropertySupplier implements ByteValuePropertySupplier {

    static final String TEST_BYTE_PROPERTY = "test.byte.property";

    @Override
    public String getName() {
        return TEST_BYTE_PROPERTY;
    }
}
