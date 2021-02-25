package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.MultipleObjectPropertySupplier;

import java.util.function.Supplier;

public class TestObjectsSupplier implements MultipleObjectPropertySupplier<Object, Supplier<Object>> {

    static final String TEST_OBJECTS_PROPERTY = "test.objects.property";

    @Override
    public String getName() {
        return TEST_OBJECTS_PROPERTY;
    }
}
