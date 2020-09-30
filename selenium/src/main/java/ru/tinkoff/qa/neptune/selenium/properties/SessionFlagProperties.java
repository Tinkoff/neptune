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

    @PropertyDescription(description = {"It means that it will try to clear WebDriver cookies after the test running has finished if 'true'",
            "This option has sense if KEEP_WEB_DRIVER_SESSION_OPENED has value 'true'"},
            section = "Selenium. Session flags")
    @PropertyName("CLEAR_WEB_DRIVER_COOKIES")
    CLEAR_WEB_DRIVER_COOKIES,

    @PropertyDescription(description = {"It means that it will try to get to default application URL if it is defined.",
            "This option has sense when 'KEEP_WEB_DRIVER_SESSION_OPENED' has value 'true' and 'BASE_WEB_DRIVER_URL' is defined"},
            section = "Selenium. Session flags")
    @PropertyName("GET_BACK_TO_BASE_WEB_DRIVER_URL")
    GET_BACK_TO_BASE_URL,

    @PropertyDescription(description = "It means that window/tab is forcefully maximized when browser is started",
            section = "Selenium. Session flags")
    @PropertyName("FORCE_WINDOW_MAXIMIZING_ON_START")
    FORCE_WINDOW_MAXIMIZING_ON_START,

    @PropertyDescription(description = {"It means that it is possible to navigate",
            "with only part of an url. It should be relative to value defined by 'BASE_WEB_DRIVER_URL'"},
            section = "Selenium. Session flags")
    @PropertyName("ENABLE_TO_NAVIGATE_BY_RELATIVE_URL")
    ENABLE_ABILITY_TO_NAVIGATE_BY_RELATIVE_URL,

    @PropertyDescription(description = {"It means that WebDriver session will be using browser proxy"},
            section = "Selenium. Session flags")
    @PropertyName("USE_BROWSER_PROXY")
    USE_BROWSER_PROXY
}
