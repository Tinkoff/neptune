package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;

@PropertyName("SOME_STR_PROPERTY")
@PropertyDefaultValue("Some String")
public class PropertyStringWithDefaultValue implements StringValuePropertySupplier {
}
