package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

public class TestEnumItemPropertySuppler implements EnumPropertySuppler<TestEnum> {

    static final String TEST_ENUM_ITEM_PROPERTY = "test.enum.item.property";

    @Override
    public String getName() {
        return TEST_ENUM_ITEM_PROPERTY;
    }
}
