package com.github.toy.constructor.selenium;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

public class SeleniumSteps implements PerformStep<SeleniumSteps>, GetStep<SeleniumSteps>, WrapsDriver{

    private final WebDriver driver;

    public SeleniumSteps(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
