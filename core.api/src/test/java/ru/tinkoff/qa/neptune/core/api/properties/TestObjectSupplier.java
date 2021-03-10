package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

public class TestObjectSupplier implements ObjectPropertySupplier<Object, Supplier<Object>> {

    static final String TEST_OBJECT_PROPERTY = "test.object.property";

    @Override
    public String getName() {
        return TEST_OBJECT_PROPERTY;
    }
}
