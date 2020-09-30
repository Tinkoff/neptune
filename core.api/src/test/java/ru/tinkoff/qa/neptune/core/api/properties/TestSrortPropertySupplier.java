package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.shorts.ShortValuePropertySupplier;

public class TestSrortPropertySupplier implements ShortValuePropertySupplier {

    static final String TEST_SHORT_PROPERTY = "test.short.property";

    @Override
    public String getName() {
        return TEST_SHORT_PROPERTY;
    }
}
