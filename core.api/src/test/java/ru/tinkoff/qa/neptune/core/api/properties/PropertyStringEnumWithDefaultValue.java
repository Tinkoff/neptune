package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;

public enum PropertyStringEnumWithDefaultValue implements StringValuePropertySupplier {
    @PropertyName("SOME_STR_PROPERTY2")
    @PropertyDefaultValue("Some String 2")
    STR_VALUE
}
