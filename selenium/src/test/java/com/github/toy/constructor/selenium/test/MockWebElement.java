package com.github.toy.constructor.selenium.test;

import org.openqa.selenium.*;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MockWebElement implements WebElement {

    public static final String VALUE = "value";

    final By foundBy;
    private final Map<String, String> attributes;
    private final Map<String, String> css;
    private final Point location;
    private final Dimension size;
    private final boolean isDisplayed;
    private final boolean isFlag;
    private boolean isSelected;
    private final String tagName;
    private final String text;
    private final List<MockWebElement> children;
    private int clickCount;

    public MockWebElement(By foundBy, Map<String, String> attributes, Map<String, String> css, Point location,
                          Dimension size, boolean isDisplayed, boolean isFlag, String tagName, String text, List<MockWebElement> children) {
        this.foundBy = foundBy;
        this.attributes = attributes;
        this.css = css;
        this.location = location;
        this.size = size;
        this.isDisplayed = isDisplayed;
        this.isFlag = isFlag;
        this.tagName = tagName;
        this.text = text;
        this.children = children;
    }

    @Override
    public void click() {
        if (isFlag) {
            isSelected = !isSelected;
        }
        clickCount++;
    }

    @Override
    public void submit() {
        click();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        if (attributes.keySet().contains(VALUE)) {
            if (keysToSend.length == 1) {
                attributes.put(VALUE, valueOf(keysToSend[0]));
            }

            if (keysToSend.length > 0) {
                stream(keysToSend).filter(charSequence -> String.class.isAssignableFrom(charSequence.getClass()))
                        .findFirst().ifPresent(charSequence -> attributes.put(VALUE, valueOf(charSequence)));
            }
        }
    }

    @Override
    public void clear() {
        if (attributes.keySet().contains(VALUE)) {
            attributes.put(VALUE, EMPTY);
        }
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public String getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return children.stream().filter(mockWebElement -> mockWebElement.foundBy.equals(by)).collect(toList());
    }

    @Override
    public WebElement findElement(By by) {
        List<WebElement> result = findElements(by);
        if (result.size() == 0) {
            throw new NoSuchElementException(format("Can't locate element with locator %s", by));
        }

        return result.get(0);
    }

    @Override
    public boolean isDisplayed() {
        return isDisplayed;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(location, size);
    }

    @Override
    public String getCssValue(String propertyName) {
        return css.get(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return target.convertFromPngBytes(new byte[]{1,2});
    }

    public int getClickCount() {
        return clickCount;
    }
}
