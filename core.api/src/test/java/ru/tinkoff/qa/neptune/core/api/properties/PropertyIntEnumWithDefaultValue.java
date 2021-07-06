package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.integers.IntValuePropertySupplier;

public enum PropertyIntEnumWithDefaultValue implements IntValuePropertySupplier {
    @PropertyName("SOME_INT_PROPERTY2")
    @PropertyDefaultValue("2")
    INT_VALUE
}
