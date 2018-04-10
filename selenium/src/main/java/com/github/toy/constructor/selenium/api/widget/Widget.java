package com.github.toy.constructor.selenium.api.widget;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.util.List;

/**
 * This class is designed to represent hierarchical structure
 * of related elements. It is expected that there should be some top-level
 * element from which other elements can be found.
 */
public abstract class Widget implements WrapsElement, SearchContext, TakesScreenshot, HasAttribute,
        IsEnabled, IsVisible, HasSize, HasRectangle, HasLocation, HasCssValue {

    private final WebElement wrappedElement;

    public Widget(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    public static String getWidgetName(Class<? extends Widget> classOfAWidget) {
        Class<?> clazz = classOfAWidget;
        while (!clazz.equals(Widget.class)) {
            Name name =  classOfAWidget.getAnnotation(Name.class);
            if (name != null) {
                return name.value();
            }
            clazz = clazz.getSuperclass();
        }
        return classOfAWidget.getName();
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

    @Override
    public Point getLocation() {
        return wrappedElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        return wrappedElement.getSize();
    }

    @Override
    public Rectangle getRect() {
        return wrappedElement.getRect();
    }

    @Override
    public String getAttribute(String attribute) {
        return wrappedElement.getAttribute(attribute);
    }

    @Override
    public String getCssValue(String propertyName) {
        return wrappedElement.getCssValue(propertyName);
    }

    @Override
    public boolean isVisible() {
        return wrappedElement.isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        return wrappedElement.isEnabled();
    }
}
