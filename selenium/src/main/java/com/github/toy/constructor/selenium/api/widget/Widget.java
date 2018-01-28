package com.github.toy.constructor.selenium.api.widget;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.util.List;

public abstract class Widget implements WrapsElement, SearchContext, TakesScreenshot {

    private final WebElement wrappedElement;

    public Widget(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return wrappedElement.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return wrappedElement.findElement(by);
    }

    @Override
    public WebElement getWrappedElement() {
        return wrappedElement;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return wrappedElement.getScreenshotAs(target);
    }

    public Point getLocation() {
        return wrappedElement.getLocation();
    }

    public Dimension getSize() {
        return wrappedElement.getSize();
    }

    public Rectangle getRect() {
        return wrappedElement.getRect();
    }

    public abstract boolean isVisible();

    public abstract boolean isEnabled();
}
