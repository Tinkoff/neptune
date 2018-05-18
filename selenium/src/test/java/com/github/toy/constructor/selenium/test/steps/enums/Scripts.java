package com.github.toy.constructor.selenium.test.steps.enums;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public enum Scripts {
    SCRIPT_1("Script #1") {
        @Override
        public String execute(Object... args) {
            return String.join(",", stream(args).map(Object::toString).collect(Collectors.toList()));
        }
    },

    SCRIPT_2("Script #1") {
        @Override
        public Object execute(Object... args) {
            return new WebElement() {
                @Override
                public void click() {

                }

                @Override
                public void submit() {

                }

                @Override
                public void sendKeys(CharSequence... keysToSend) {

                }

                @Override
                public void clear() {

                }

                @Override
                public String getTagName() {
                    return null;
                }

                @Override
                public String getAttribute(String name) {
                    return null;
                }

                @Override
                public boolean isSelected() {
                    return false;
                }

                @Override
                public boolean isEnabled() {
                    return false;
                }

                @Override
                public String getText() {
                    return null;
                }

                @Override
                public List<WebElement> findElements(By by) {
                    return null;
                }

                @Override
                public WebElement findElement(By by) {
                    return null;
                }

                @Override
                public boolean isDisplayed() {
                    return false;
                }

                @Override
                public Point getLocation() {
                    return null;
                }

                @Override
                public Dimension getSize() {
                    return null;
                }

                @Override
                public Rectangle getRect() {
                    return null;
                }

                @Override
                public String getCssValue(String propertyName) {
                    return null;
                }

                @Override
                public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
                    return null;
                }

                @Override
                public String toString() {
                    return String.join(",", stream(args).map(Object::toString).collect(Collectors.toList()));
                }
            };
        }
    },

    SCRIPT_3("Script #3") {
        @Override
        public String execute(Object... args) {
            throw new WebDriverException(format("It is not possible to execute script %s with parameters %s",
                    getScript(),  ArrayUtils.toString(args)));
        }
    };

    private final String script;

    Scripts(String script) {
        this.script = script;
    }

    public abstract Object execute(Object... args);

    public String getScript() {
        return script;
    }
}
