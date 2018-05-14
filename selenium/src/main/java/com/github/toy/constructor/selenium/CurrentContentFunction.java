package com.github.toy.constructor.selenium;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;

public final class CurrentContentFunction implements Function<SeleniumSteps, WebDriver> {

    public static Function<SeleniumSteps, WebDriver> currentContent() {
        return toGet("Current content", new CurrentContentFunction());
    }

    private CurrentContentFunction() {
        super();
    }

    @Override
    public WebDriver apply(SeleniumSteps seleniumSteps) {
        return seleniumSteps.getWrappedDriver();
    }
}
