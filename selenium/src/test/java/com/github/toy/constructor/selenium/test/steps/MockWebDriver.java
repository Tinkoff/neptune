package com.github.toy.constructor.selenium.test.steps;

import org.openqa.selenium.*;

import java.util.*;

import static com.github.toy.constructor.selenium.test.steps.MockTargetLocator.WINDOW_HANDLES;
import static com.github.toy.constructor.selenium.test.steps.URLs.*;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class MockWebDriver implements WebDriver, JavascriptExecutor {

    private final MockTargetLocator targetLocator = new MockTargetLocator(this);
    private final MockNavigation navigation = new MockNavigation(this);

    final Map<String, LinkedList<URLs>> handlesAndUrlHistory = new HashMap<>() {
        {
            put(WINDOW_HANDLES[0], new LinkedList<>(List.of(BLANK)));
            put(WINDOW_HANDLES[1], new LinkedList<>(List.of(BLANK)));
            put(WINDOW_HANDLES[2], new LinkedList<>(List.of(BLANK)));
        }
    };

    final Map<String, URLs> currentUrls = new HashMap<>() {
        {
            put(WINDOW_HANDLES[0], BLANK);
            put(WINDOW_HANDLES[1], BLANK);
            put(WINDOW_HANDLES[2], BLANK);
        }
    };

    String getMockHandle() {
        return ofNullable(targetLocator.currentHandle)
                .orElseThrow(() ->
                        new WebDriverException("The window which was in focus before has been closed"));
    }

    void addUrlToHistory(String handle, URLs url) {
        LinkedList<URLs> history = handlesAndUrlHistory.get(handle);
        if (!history.getLast().equals(url)) {
            history.addLast(url);
        }
    }

    void changeCurrentUrl(String handle, URLs url) {
        currentUrls.put(handle, url);
    }

    @Override
    public void get(String url) {
        String handle = getMockHandle();
        URLs urlEnum = stream(URLs.values())
                .filter(urLs -> url.equals(urLs.getUrl())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("Unknown url %s", url)));
        addUrlToHistory(handle, urlEnum);
        changeCurrentUrl(handle, urlEnum);
    }

    @Override
    public String getCurrentUrl() {
        return currentUrls.get(getMockHandle()).getUrl();
    }

    @Override
    public String getTitle() {
        return currentUrls.get(getMockHandle()).getTitle();
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
        handlesAndUrlHistory.remove(targetLocator.currentHandle);
    }

    @Override
    public void quit() {
        handlesAndUrlHistory.clear();
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
        return navigation;
    }

    @Override
    public Options manage() {
        return null;
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return null;
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return null;
    }
}
