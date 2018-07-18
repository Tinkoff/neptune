package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;

public enum  URLProperties implements PropertySupplier<URL> {
    /**
     * This item read the property {@code 'remote.web.driver.url'} and returns URL to start
     * a new remote session of {@link org.openqa.selenium.WebDriver}
     */
    REMOTE_WEB_DRIVER_URL_PROPERTY("remote.web.driver.url"),

    /**
     * This item read the property {@code 'base.web.driver.url'} and returns URL to load at
     * browser when it is started.
     * @see org.openqa.selenium.WebDriver#get(String)
     */
    BASE_WEB_DRIVER_URL_PROPERTY("base.web.driver.url");

    private final String propertyName;

    URLProperties(String propertyName) {
        this.propertyName = propertyName;
    }


    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public URL get() {
        return returnOptionalFromEnvironment()
                .map(s -> {
                    try {
                        return new URL(s);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(format("URL %s is malformed", s), e);
                    }
                }).orElse(null);
    }
}
