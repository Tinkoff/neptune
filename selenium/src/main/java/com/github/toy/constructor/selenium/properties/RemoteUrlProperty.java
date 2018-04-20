package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.PropertySupplier;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;

public final class RemoteUrlProperty implements PropertySupplier<URL> {

    private static final String URL_PROPERTY = "remote.web.driver.url";

    /**
     * This item read the property {@code 'remote.web.driver.url'} and returns URL to start
     * a new remote session of {@link org.openqa.selenium.WebDriver}
     */
    public static final RemoteUrlProperty REMOTE_WEB_DRIVER_URL_PROPERTY = new RemoteUrlProperty();

    private RemoteUrlProperty() {
        super();
    }


    @Override
    public String getPropertyName() {
        return URL_PROPERTY;
    }

    @Override
    public URL get() {
        return returnOptional()
                .map(s -> {
                    try {
                        return new URL(s);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(format("URL %s is malformed", s), e);
                    }
                }).orElse(null);
    }
}
