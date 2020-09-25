package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.enums.MultipleEnumPropertySuppler;

public class TestEnumItemsPropertySuppler implements MultipleEnumPropertySuppler<TestEnum> {

    static final String TEST_ENUM_ITEMS_PROPERTY = "test.enum.items.property";

    @Override
    public String getName() {
        return TEST_ENUM_ITEMS_PROPERTY;
    }
}
