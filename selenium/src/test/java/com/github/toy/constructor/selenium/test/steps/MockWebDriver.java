package com.github.toy.constructor.selenium.test.steps;

import com.github.toy.constructor.selenium.test.steps.enums.Scripts;
import com.github.toy.constructor.selenium.test.steps.enums.URLs;
import com.github.toy.constructor.selenium.test.steps.enums.WindowHandles;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;

import java.util.*;

import static com.github.toy.constructor.selenium.test.steps.enums.URLs.*;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class MockWebDriver implements WebDriver, JavascriptExecutor {

    private final MockTargetLocator targetLocator = new MockTargetLocator(this);
    private final MockNavigation navigation = new MockNavigation(this);
    private final MockOptions options = new MockOptions(this);

    final Map<String, LinkedList<URLs>> handlesAndUrlHistory = new HashMap<>() {
        {
            put(WindowHandles.HANDLE1.getHandle(), new LinkedList<>(List.of(BLANK)));
            put(WindowHandles.HANDLE2.getHandle(), new LinkedList<>(List.of(BLANK)));
            put(WindowHandles.HANDLE3.getHandle(), new LinkedList<>(List.of(BLANK)));
        }
    };

    final Map<String, URLs> currentUrls = new HashMap<>() {
        {
            put(WindowHandles.HANDLE1.getHandle(), BLANK);
            put(WindowHandles.HANDLE2.getHandle(), BLANK);
            put(WindowHandles.HANDLE3.getHandle(), BLANK);
        }
    };

    private String getMockHandle() {
        return ofNullable(targetLocator.currentHandle)
                .orElseThrow(() ->
                        new WebDriverException("The window which was in focus before has been closed"));
    }

    private void addUrlToHistory(String handle, URLs url) {
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
        targetLocator.handles.remove(targetLocator.currentHandle);
        currentUrls.remove(targetLocator.currentHandle);
    }

    @Override
    public void quit() {
        handlesAndUrlHistory.clear();
        targetLocator.handles.clear();
        currentUrls.clear();
    }

    @Override
    public Set<String> getWindowHandles() {
        return Set.of(targetLocator.handles.toArray(new String[] {}));
    }

    @Override
    public String getWindowHandle() {
        return getMockHandle();
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
        return options;
    }

    private Object executeMockScripts(String script, Object... args) {
        return stream(Scripts.values()).filter(scripts -> script.equals(scripts.getScript()))
                .findFirst().map(scripts -> scripts.execute(args))
                .orElseThrow(() -> new UnsupportedOperationException(format("%s can't be executed with parameters %s",
                        script, ArrayUtils.toString(args))));
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return executeMockScripts(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return executeMockScripts(script, args);
    }
}
