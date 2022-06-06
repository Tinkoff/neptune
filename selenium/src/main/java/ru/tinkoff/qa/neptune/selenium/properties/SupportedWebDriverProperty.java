package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

@PropertyDescription(description = {"Defines WebDriver to be started.",
        "Available values: REMOTE_DRIVER, CHROME_DRIVER, EDGE_DRIVER, FIREFOX_DRIVER, IE_DRIVER, SAFARI_DRIVER"},
        section = "Selenium. Web driver to launch")
@PropertyName("WEB_DRIVER_TO_LAUNCH")
@PropertyDefaultValue("CHROME_DRIVER")
public final class SupportedWebDriverProperty implements EnumPropertySuppler<SupportedWebDrivers> {

    public static final SupportedWebDriverProperty SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY =
            new SupportedWebDriverProperty();

    private SupportedWebDriverProperty() {
        super();
    }
}
