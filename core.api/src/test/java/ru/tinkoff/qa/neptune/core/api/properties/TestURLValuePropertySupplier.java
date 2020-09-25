package ru.tinkoff.qa.neptune.core.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

public class TestURLValuePropertySupplier implements URLValuePropertySupplier {

    static final String TEST_URL_PROPERTY = "test.url.property";

    @Override
    public String getName() {
        return TEST_URL_PROPERTY;
    }
}
