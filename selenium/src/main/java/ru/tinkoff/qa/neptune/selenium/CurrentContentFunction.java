package ru.tinkoff.qa.neptune.selenium;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public final class CurrentContentFunction implements Function<SeleniumSteps, WebDriver> {

    public static Function<SeleniumSteps, WebDriver> currentContent() {
        return new CurrentContentFunction();
    }

    private CurrentContentFunction() {
        super();
    }

    @Override
    public WebDriver apply(SeleniumSteps seleniumSteps) {
        return seleniumSteps.getWrappedDriver();
    }
}
