package ru.tinkoff.qa.neptune.selenium.test;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;
import ru.tinkoff.qa.neptune.selenium.test.enums.Scripts;
import ru.tinkoff.qa.neptune.selenium.test.enums.URLs;
import ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles;

import java.io.IOException;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.getFakeDOM;
import static ru.tinkoff.qa.neptune.selenium.test.SequenceSpy.setActions;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.BLANK;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.values;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE1;

public class MockWebDriver implements WebDriver, JavascriptExecutor, TakesScreenshot, Interactive {

    private final MockTargetLocator targetLocator = new MockTargetLocator(this);
    private final MockNavigation navigation = new MockNavigation(this);
    private final MockOptions options = new MockOptions(this);
    private final List<MockWebElement> children;

    private boolean isSwitchedToDefaultContent;
    private boolean isSwitchedToParentFrame;
    private Object currentFrame;
    public String lastNavigationURLAsIs;

    final Map<String, LinkedList<URLs>> handlesAndUrlHistory = new HashMap<>() {
        {
            put(HANDLE1.getHandle(), new LinkedList<>(List.of(BLANK)));
            put(WindowHandles.HANDLE2.getHandle(), new LinkedList<>(List.of(BLANK)));
            put(WindowHandles.HANDLE3.getHandle(), new LinkedList<>(List.of(BLANK)));
        }
    };

    final Map<String, URLs> currentUrls = new HashMap<>() {
        {
            put(HANDLE1.getHandle(), BLANK);
            put(WindowHandles.HANDLE2.getHandle(), BLANK);
            put(WindowHandles.HANDLE3.getHandle(), BLANK);
        }
    };

    private MockWebDriver(List<MockWebElement> children) {
        this.children = children;
        children.forEach(mockWebElement -> mockWebElement.setDriver(this));
    }

    public MockWebDriver() {
        this(getFakeDOM());
    }

    public String getMockHandle() {
        return ofNullable(targetLocator.currentHandle)
                .orElse(HANDLE1.getHandle());
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
        URLs urlEnum = stream(values())
                .filter(urLs ->
                        !urLs.equals(BLANK) &&
                                (url.equals(urLs.getUrl()) || url.contains(urLs.getUrl()) || urLs.getUrl().startsWith(url))
                ).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("Unknown url %s", url)));
        addUrlToHistory(handle, urlEnum);
        changeCurrentUrl(handle, urlEnum);
        lastNavigationURLAsIs = url;
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
        List<WebElement> elements = new LinkedList<>();
        children.forEach(mockWebElement -> {
            if (mockWebElement.foundBy.equals(by)) {
                elements.add(mockWebElement);
            }
            elements.addAll(mockWebElement.findElements(by));
        });
        return elements;
    }

    @Override
    public WebElement findElement(By by) {
        List<WebElement> result = findElements(by);
        if (result.isEmpty()) {
            throw new NoSuchElementException(format("Can't locate element with locator %s", by));
        }

        return result.get(0);
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
        return new LinkedHashSet<>(targetLocator.handles);
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
        return Arrays.stream(Scripts.values()).filter(scripts -> script.equals(scripts.getScript()))
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

    public boolean isSwitchedToDefaultContent() {
        return isSwitchedToDefaultContent;
    }

    WebDriver setSwitchedToDefaultContent() {
        isSwitchedToDefaultContent = true;
        return this;
    }

    public boolean isSwitchedToParentFrame() {
        return isSwitchedToParentFrame;
    }

    public WebDriver setSwitchedToParentFrame(boolean switched) {
        isSwitchedToParentFrame = switched;
        return this;
    }

    public Object getCurrentFrame() {
        return currentFrame;
    }

    WebDriver setFrame(Object frame) {
        this.currentFrame = frame;
        return this;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        try {
            return target.convertFromPngBytes(requireNonNull(this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("black.jpeg"))
                .readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void perform(Collection<Sequence> actions) {
        setActions(actions);
    }

    @Override
    public void resetInputState() {
    }
}
