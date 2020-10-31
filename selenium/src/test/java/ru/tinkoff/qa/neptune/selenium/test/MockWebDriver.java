package ru.tinkoff.qa.neptune.selenium.test;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Sequence;
import ru.tinkoff.qa.neptune.selenium.test.enums.Scripts;
import ru.tinkoff.qa.neptune.selenium.test.enums.URLs;
import ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles;

import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.getFakeDOM;
import static ru.tinkoff.qa.neptune.selenium.test.SequenceSpy.setActions;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.BLANK;
import static ru.tinkoff.qa.neptune.selenium.test.enums.URLs.values;

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

    private MockWebDriver(List<MockWebElement> children) {
        this.children = children;
        children.forEach(mockWebElement -> mockWebElement.setDriver(this));
    }

    public MockWebDriver() {
        this(getFakeDOM());
    }

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
        if (result.size() == 0) {
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

    WebDriver setSwitchedToParentFrame() {
        isSwitchedToParentFrame = true;
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

    @Override
    public void perform(Collection<Sequence> actions) {
        setActions(actions);
    }

    @Override
    public void resetInputState() {
    }
}
