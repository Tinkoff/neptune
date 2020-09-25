package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

public enum URLProperties implements URLValuePropertySupplier {
    /**
     * This item read the property {@code 'REMOTE_WEB_DRIVER_URL'} and returns URL to start
     * a new remote session of {@link org.openqa.selenium.WebDriver}
     */
    @PropertyDescription(description = "Defines URL of a remote server (Selenium Grid, Selenoid etc.) to start a new browser remotely",
            section = "Selenium. URLS")
    @PropertyName("REMOTE_WEB_DRIVER_URL")
    REMOTE_WEB_DRIVER_URL_PROPERTY,

    @PropertyDescription(description = "Defines default application URL", section = "Selenium. URLS")
    @PropertyName("BASE_WEB_DRIVER_URL")
    BASE_WEB_DRIVER_URL_PROPERTY
}
