package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.LOWEST;

/**
 * This class is designed to represent hierarchical structure
 * of related elements. It is expected that there should be some top-level
 * element from which other elements can be found.
 */
@Priority(LOWEST)
public abstract class Widget implements WrapsElement, SearchContext, TakesScreenshot, HasAttribute,
        IsEnabled, IsVisible, HasSize, HasRectangle, HasLocation, HasCssValue {

    private final SearchContext wrappedElement;

    public Widget(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    public static String getWidgetName(Class<? extends Widget> classOfAWidget) {
        Class<?> clazz = classOfAWidget;
        while (!clazz.equals(Widget.class)) {
            var name =  clazz.getAnnotation(Name.class);
            if (name != null) {
                return name.value();
            }
            clazz = clazz.getSuperclass();
        }
        return classOfAWidget.getSimpleName();
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
        return (WebElement) wrappedElement;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((WebElement) wrappedElement).getScreenshotAs(target);
    }

    @Override
    public Point getLocation() {
        return ((WebElement) wrappedElement).getLocation();
    }

    @Override
    public Dimension getSize() {
        return ((WebElement) wrappedElement).getSize();
    }

    @Override
    public Rectangle getRect() {
        return ((WebElement) wrappedElement).getRect();
    }

    @Override
    public String getAttribute(String attribute) {
        return ((WebElement) wrappedElement).getAttribute(attribute);
    }

    @Override
    public String getCssValue(String propertyName) {
        return ((WebElement) wrappedElement).getCssValue(propertyName);
    }

    @Override
    public boolean isVisible() {
        return ((WebElement) wrappedElement).isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        return ((WebElement) wrappedElement).isEnabled();
    }
}
