package com.github.toy.constructor.selenium.api.widget;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;

import java.util.List;

/**
 * This class is designed to represent hierarchical structure
 * of related elements. It is expected that there should be some top-level
 * element from which other elements can be found.
 */
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

    /**
     * Where on the page is the top left-hand corner of the rendered element?
     *
     * @return A point, containing the location of the top left-hand corner of the element
     */
    public Point getLocation() {
        return wrappedElement.getLocation();
    }

    /**
     * It is the width and height of the rendered element
     *
     * @return The size of the element on the page.
     */
    public Dimension getSize() {
        return wrappedElement.getSize();
    }

    /**
     * @return The location and size of the rendered element
     */
    public Rectangle getRect() {
        return wrappedElement.getRect();
    }

    /**
     * Get the value of the given attribute of the element. Will return the current value, even if
     * this has been modified after the page has been loaded.
     *
     * <p>More exactly, this method will return the value of the property with the given name, if it
     * exists. If it does not, then the value of the attribute with the given name is returned. If
     * neither exists, null is returned.
     *
     * <p>The "style" attribute is converted as best can be to a text representation with a trailing
     * semi-colon.
     *
     * <p>The following are deemed to be "boolean" attributes, and will return either "true" or null:
     *
     * <p>async, autofocus, autoplay, checked, compact, complete, controls, declare, defaultchecked,
     * defaultselected, defer, disabled, draggable, ended, formnovalidate, hidden, indeterminate,
     * iscontenteditable, ismap, itemscope, loop, multiple, muted, nohref, noresize, noshade,
     * novalidate, nowrap, open, paused, pubdate, readonly, required, reversed, scoped, seamless,
     * seeking, selected, truespeed, willvalidate
     *
     * <p>Finally, the following commonly mis-capitalized attribute/property names are evaluated as
     * expected:
     *
     * <ul>
     * <li>If the given name is "class", the "className" property is returned.
     * <li>If the given name is "readonly", the "readOnly" property is returned.
     * </ul>
     *
     * <i>Note:</i> The reason for this behavior is that users frequently confuse attributes and
     * properties. If you need to do something more precise, e.g., refer to an attribute even when a
     * property of the same name exists, then you should evaluate Javascript to obtain the result
     * you desire.
     *
     * @param attribute The name of the attribute.
     * @return The attribute/property's current value or null if the value is not set.
     */
    public String getAttribute(String attribute) {
        return wrappedElement.getAttribute(attribute);
    }

    /**
     * @return Whether or not the element is visible on the page currently.
     */
    public boolean isVisible() {
        return wrappedElement.isDisplayed();
    }

    /**
     * @return True if the element is enabled currently, false otherwise.
     */
    public boolean isEnabled() {
        return wrappedElement.isEnabled();
    }
}
