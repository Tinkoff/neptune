package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

public enum URLProperties implements URLValuePropertySupplier {
    @PropertyDescription(description = "Defines URL of a remote server (Selenium Grid, Selenoid etc.) to start a new browser remotely",
            section = "Selenium. URLS")
    @PropertyName("REMOTE_WEB_DRIVER_URL")
    REMOTE_WEB_DRIVER_URL_PROPERTY,

    @PropertyDescription(description = "Defines default application URL", section = "Selenium. URLS")
    @PropertyName("BASE_WEB_DRIVER_URL")
    BASE_WEB_DRIVER_URL_PROPERTY,

    @PropertyDescription(description = "Defines external proxy url", section = "Selenium. URLs")
    @PropertyName("PROXY_URL_PROPERTY")
    PROXY_URL_PROPERTY
}
