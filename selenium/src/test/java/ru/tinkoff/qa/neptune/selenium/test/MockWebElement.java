package ru.tinkoff.qa.neptune.selenium.test;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ByAll;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.VALUE;

public class MockWebElement implements WebElement, WrapsDriver {

    private WebDriver driver;
    final By foundBy;
    private final Map<String, String> attributes;
    private final Map<String, String> css;
    private final Point location;
    private final Dimension size;
    private final boolean isDisplayed;
    private final boolean isEnabled;
    private final boolean isFlag;
    private boolean isSelected;
    private final String tagName;
    private final String text;
    private final List<MockWebElement> children;
    private int clickCount;
    private int scrollCount;

    MockWebElement(By foundBy, Map<String, String> attributes, Map<String, String> css, Point location,
                   Dimension size, boolean isDisplayed, boolean isEnabled, boolean isFlag, String tagName, String text,
                   List<MockWebElement> children) {
        this.foundBy = foundBy;
        this.attributes = new HashMap<>(attributes);
        this.css = new HashMap<>(css);
        this.location = location;
        this.size = size;
        this.isDisplayed = isDisplayed;
        this.isEnabled = isEnabled;
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
        clickCount = clickCount + 1;
    }

    @Override
    public void submit() {
        click();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        if (keysToSend.length == 1) {
            attributes.put(VALUE, valueOf(keysToSend[0]));
        }

        if (keysToSend.length > 0) {
            stream(keysToSend).filter(charSequence -> String.class.isAssignableFrom(charSequence.getClass()))
                    .findFirst().ifPresent(charSequence -> attributes.put(VALUE, valueOf(charSequence)));
        }
    }

    @Override
    public void clear() {
        if (attributes.containsKey(VALUE)) {
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
        return isEnabled;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<WebElement> findElements(By by) {
        List<WebElement> elements = new LinkedList<>();

        children.forEach(mockWebElement -> {
            if (!mockWebElement.foundBy.getClass().equals(ByAll.class)) {
                if (mockWebElement.foundBy.equals(by)) {
                    elements.add(mockWebElement);
                }
            } else {
                try {
                    var bys = ByAll.class.getDeclaredField("bys");
                    bys.setAccessible(true);
                    var foundBy = (By[]) bys.get(mockWebElement.foundBy);

                    if (ArrayUtils.contains(foundBy, by)) {
                        elements.add(mockWebElement);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            elements.addAll(mockWebElement.findElements(by).stream().map(webElement -> ((MockWebElement) webElement)
                    .setDriver(driver)).collect(toList()));
        });
        return elements;
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
        String base64EncodedPng = "iVBORw0KGgoAAAANSUhEUgAAAHUAAABkCAIAAAA37OjVAAAAAXNSR0IArs4c6QAAAARnQU1BAACx\n" +
                "jwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAANCSURBVHhe7do/a1NRHMZxX4hO6qJTumXrJgiC\n" +
                "i5NOdmqn0kUcChVcHQQHwUFwcBDcxUW6iC9AfBEFE9K/scXGJ5zD8XDOrQn1Pr9zkvt8OYO5bSL5\n" +
                "9Jf7r70yUczky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7cuuh7+mV3/8HD\n" +
                "8YeP/jGzzvkCd3Br5efVG4Prt/0mZt3yxcw6XLf8VmYd8j159z7IYh093fZfYNYh32Gvb4yLjHyx\n" +
                "1xv2V7GOdp7/3tvzWw07fvEy4J6Px34rPyNfyIa3hwPL8avX/gsmxbiHG5t+q0lGvgAN7xDL5tiN\n" +
                "zkcjfGLC/7v/6LHl8CLT/e+vT58h694qZspvpYUDWny2YI+LrI9v8TTxiHEeFu+RsIrgImtfvEm8\n" +
                "1fC2Wyd2B9Lw+lh4aHOp1pi1LyIR5zOLhaNZkbENFfBFrRPjFcKruVVc1lXGF7VCnO8N3DK7fJhZ\n" +
                "MV+UEANlzkuPs+8/cJzMZfFqNcxsXElflBDPvPTAKVfjwGJVskNIKuyLgHKwth5L5YPceOwKq569\n" +
                "QV55XxcuPUZ37gWyMMiNssNeH6anX7+559ZcLb4oH+TR3fvxQ7fq3A9cVEW+rr+DfO3m4rKGqvOd\n" +
                "3pHZfhbLutX6lZ5Ndfkmd2SwBtG/F5G4Ft+L7shgxSdw+B78DPxzFqHyvsktWocY35FJiLEWaJBL\n" +
                "+s5/RwZbDreexN+W/AyqrZhvfkdm5tVtMsg4R67/LLiAbz628w9jPsjTp/f6J2/e+u+oLGvfZGxn\n" +
                "zmxjyW/zsDDLdR737Hzze4n/ecng7qKFX+hhVXjcs/ON/7zjcmPbWDLLtRHb+YZBa/1KF68WH/fw\n" +
                "KalnX2Hn6waNdC8xIcb6931ks6yPb7xAHJ9a4OPS7qfkci2Pr2uqvLEJX1j7TUVbNl9XkT8hbGw5\n" +
                "fetJvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTL\n" +
                "Tb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+\n" +
                "3OTLTb7MJpM/U7hf1q6DGbAAAAAASUVORK5CYII=";
        return target.convertFromBase64Png(base64EncodedPng);
    }

    public int getClickCount() {
        return clickCount;
    }

    MockWebElement setDriver(WebDriver driver) {
        this.driver = driver;
        return this;
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }

    public int getScrollCount() {
        return scrollCount;
    }

    public void scroll() {
        this.scrollCount++;
    }
}
