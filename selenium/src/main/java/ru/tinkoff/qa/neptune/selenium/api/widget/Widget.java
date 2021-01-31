package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.LOWEST;

/**
 * This class is designed to represent hierarchical structure
 * of related elements. It is expected that there should be some top-level
 * element from which other elements can be found.
 */
@Priority(LOWEST)
public abstract class Widget implements WrapsElement, SearchContext, HasAttribute,
        IsEnabled, IsVisible, HasSize, HasRectangle, HasLocation, HasCssValue, HasTextContent {

    private final SearchContext wrappedElement;

    public Widget(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    public static String getWidgetName(Class<? extends Widget> classOfAWidget) {
        Class<?> clazz = classOfAWidget;
        while (!clazz.equals(Widget.class)) {
            var name = clazz.getAnnotation(Name.class);
            if (nonNull(name)) {
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

    /**
     * Returns wrapped WebDriver
     *
     * @return wrapped WebDriver
     */
    protected WebDriver getDriver() {
        var e = getWrappedElement();
        if (e instanceof WrapsDriver) {
            return ((WrapsDriver) e).getWrappedDriver();
        }
        return null;
    }

    /**
     * Returns wrapped js executor
     *
     * @return wrapped js executor
     */
    protected JavascriptExecutor js() {
        return ofNullable(getDriver())
                .map(webDriver -> {
                    if (webDriver instanceof JavascriptExecutor) {
                        return (JavascriptExecutor) webDriver;
                    }
                    return null;
                })
                .orElse(null);
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

    public String toString() {
        var thisClazz = this.getClass();
        var widgetName = getWidgetName(thisClazz);

        try {
            String text;
            var elementText = getWrappedElement().getText().trim();

            if (isBlank(elementText)) {
                text = EMPTY;
            } else if (elementText.length() < 30) {
                text = elementText;
            } else {
                text = format("%s...", elementText.substring(0, 30));
            }

            if (isBlank(text)) {
                return widgetName;
            }
            return format("%s [%s]", widgetName, text);
        } catch (Throwable t) {
            return widgetName;
        }
    }

    public Widget selfReference() {
        return this;
    }

    public String getText() {
        return getWrappedElement().getText();
    }
}
