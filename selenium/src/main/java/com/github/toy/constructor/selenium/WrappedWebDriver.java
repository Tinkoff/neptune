package com.github.toy.constructor.selenium;

import com.github.toy.constructor.core.api.Refreshable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

public class WrappedWebDriver implements WrapsDriver, Refreshable {

    private WebDriver driver;

    @Override
    public void refresh() {
        //TODO needs to be implemented
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
