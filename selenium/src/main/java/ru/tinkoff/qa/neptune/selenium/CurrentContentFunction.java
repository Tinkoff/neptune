package ru.tinkoff.qa.neptune.selenium;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public final class CurrentContentFunction implements Function<SeleniumStepPerformer, WebDriver> {

    public static Function<SeleniumStepPerformer, WebDriver> currentContent() {
        return new CurrentContentFunction();
    }

    private CurrentContentFunction() {
        super();
    }

    @Override
    public WebDriver apply(SeleniumStepPerformer seleniumSteps) {
        return seleniumSteps.getWrappedDriver();
    }
}
