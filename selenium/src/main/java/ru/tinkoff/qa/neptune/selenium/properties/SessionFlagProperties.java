package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;

public enum SessionFlagProperties implements BooleanValuePropertySupplier {

    @PropertyDescription(description = "It means that the searching for some elements finds only visible elements when the property is 'true'",
            section = "Selenium. Session flags")
    @PropertyName("FIND_ONLY_VISIBLE_ELEMENTS")
    FIND_ONLY_VISIBLE_ELEMENTS,

    @PropertyDescription(description = "It means that WebDriver session which has been opened is kept until all tests are finished",
            section = "Selenium. Session flags")
    @PropertyName("KEEP_WEB_DRIVER_SESSION_OPENED")
    KEEP_WEB_DRIVER_SESSION_OPENED,

    @PropertyDescription(description = "It means that window/tab is forcefully maximized when browser is started",
            section = "Selenium. Session flags")
    @PropertyName("FORCE_WINDOW_MAXIMIZING_ON_START")
    FORCE_WINDOW_MAXIMIZING_ON_START,
}
