package com.github.toy.constructor.selenium.functions.target.locator.frame;

import org.openqa.selenium.WebDriver;

class FrameParameterPOJO {
    private final WebDriver driver;
    private final Object frame;

    FrameParameterPOJO(WebDriver driver, Object frame) {
        this.driver = driver;
        this.frame = frame;
    }

    public Object getFrame() {
        return frame;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
