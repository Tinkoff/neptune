package com.github.toy.constructor.selenium.test.steps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.toy.constructor.selenium.test.steps.MockTargetLocator.WINDOW_HANDLES;
import static com.github.toy.constructor.selenium.test.steps.URLs.GITHUB;
import static com.github.toy.constructor.selenium.test.steps.URLs.GOOGLE;
import static com.github.toy.constructor.selenium.test.steps.URLs.YOUTUBE;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class MockWebDriver implements WebDriver {

    private final MockTargetLocator targetLocator = new MockTargetLocator(this);

    private Map<String, URLs> handlesAndUrls = new HashMap<>() {
        {
            put(WINDOW_HANDLES[0], GOOGLE);
            put(WINDOW_HANDLES[1], YOUTUBE);
            put(WINDOW_HANDLES[2], GITHUB);
        }
    };

    @Override
    public void get(String url) {
        handlesAndUrls.put(ofNullable(targetLocator.currentHandle)
                .orElseThrow(() -> new WebDriverException("The window which was in focus before has been closed")),
                stream(URLs.values())
                        .filter(urLs -> url.equals(urLs.getUrl())).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(format("Unknown url %s", url))));
    }

    @Override
    public String getCurrentUrl() {
        return handlesAndUrls.get(ofNullable(targetLocator.currentHandle)
                .orElseThrow(() -> new WebDriverException("The window which was in focus before has been closed")))
                .getUrl();
    }

    @Override
    public String getTitle() {
        return handlesAndUrls.get(ofNullable(targetLocator.currentHandle)
                .orElseThrow(() -> new WebDriverException("The window which was in focus before has been closed")))
                .getTitle();
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
    public String getPageSource() {
        return "<html></html>";
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return Set.of(targetLocator.handles.toArray(new String[] {}));
    }

    @Override
    public String getWindowHandle() {
        return targetLocator.currentHandle;
    }

    @Override
    public TargetLocator switchTo() {
        return targetLocator;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public Options manage() {
        return null;
    }
}
