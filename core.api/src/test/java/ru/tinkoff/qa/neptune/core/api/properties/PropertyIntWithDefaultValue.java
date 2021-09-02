package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.integers.IntValuePropertySupplier;

@PropertyName("SOME_INT_PROPERTY")
@PropertyDefaultValue("2")
public class PropertyIntWithDefaultValue implements IntValuePropertySupplier {
}
