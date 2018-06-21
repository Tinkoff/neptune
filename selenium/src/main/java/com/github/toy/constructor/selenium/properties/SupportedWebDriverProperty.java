package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.properties.PropertySupplier;

import static java.lang.String.format;

public final class SupportedWebDriverProperty implements PropertySupplier<SupportedWebDrivers> {

    /**
     * The property {@code web.driver.to.launch}. It should be defined to launch the target browser.
     * It should have the same name as one of item from {@link SupportedWebDrivers}.
     */
    public static final String WEB_DRIVER_TO_LAUNCH = "web.driver.to.launch";
    public static final SupportedWebDriverProperty SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY =
            new SupportedWebDriverProperty();

    private SupportedWebDriverProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return WEB_DRIVER_TO_LAUNCH;
    }

    @Override
    public SupportedWebDrivers get() {
        return returnOptionalFromEnvironment().map(s -> {
            for (SupportedWebDrivers supportedWebDriver: SupportedWebDrivers.values()) {
                if (supportedWebDriver.name().equalsIgnoreCase(s)) {
                    return supportedWebDriver;
                }
            }
            throw new IllegalArgumentException(format("Unknown supported web driver %s", s));
        }).orElseThrow(() ->
                new IllegalArgumentException(format("Property %s should be defined", WEB_DRIVER_TO_LAUNCH)));
    }
}
