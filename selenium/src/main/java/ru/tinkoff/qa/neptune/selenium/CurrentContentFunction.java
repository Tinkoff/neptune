package ru.tinkoff.qa.neptune.selenium;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public final class CurrentContentFunction implements Function<SeleniumStepContext, WebDriver> {

    public static Function<SeleniumStepContext, WebDriver> currentContent() {
        return new CurrentContentFunction();
    }

    private CurrentContentFunction() {
        super();
    }

    @Override
    public WebDriver apply(SeleniumStepContext seleniumSteps) {
        return seleniumSteps.getWrappedDriver();
    }
}
