package com.github.toy.constructor.selenium;

import com.github.toy.constructor.core.api.Refreshable;
import com.github.toy.constructor.selenium.properties.SupportedWebDrivers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

public class WrappedWebDriver implements WrapsDriver, Refreshable {

    private final SupportedWebDrivers supportedWebDriver;
    private WebDriver driver;

    public WrappedWebDriver(SupportedWebDrivers supportedWebDriver) {
        this.supportedWebDriver = supportedWebDriver;
    }

    @Override
    public void refresh() {
        //TODO needs to be implemented
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
