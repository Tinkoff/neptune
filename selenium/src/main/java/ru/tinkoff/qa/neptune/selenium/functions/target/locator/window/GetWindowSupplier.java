package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import java.net.URL;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.of;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_WINDOW_TIME_DURATION;

@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting for the browser window/tab")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Window criteria")
@MaxDepthOfReporting(0)
@ThrowWhenNoData(toThrow = NoSuchWindowException.class)
public final class GetWindowSupplier extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<SeleniumStepContext, Window, WebDriver, GetWindowSupplier>
        implements TargetLocatorSupplier<Window> {

    private GetWindowSupplier(Function<WebDriver, List<Window>> function) {
        super((Function<WebDriver, Iterable<Window>>) webDriver -> {
            var currentHandle = webDriver.getWindowHandle();
            try {
                return function.apply(webDriver);
            } finally {
                if (!currentHandle.equals(webDriver.getWindowHandle())) {
                    webDriver.switchTo().window(currentHandle);
                }
            }
        });
        timeOut(WAITING_WINDOW_TIME_DURATION.get());
        throwOnNoResult();
    }

    private static List<Window> getListOfWindows(WebDriver driver) {
        var windows = new LinkedList<Window>();
        for (var s : driver.getWindowHandles()) {
            windows.add(new DefaultWindow(s, driver));
        }
        return windows;
    }

    private static Window getWindowByIndex(WebDriver driver, int index) {
        var windows = getListOfWindows(driver);
        if (windows.size() >= index + 1) {
            return windows.get(index);
        }
        return null;
    }

    /**
     * Creates an instance of {@link GetWindowSupplier} to get any browser window/tab.
     * When {@link #criteria(Criteria)} or {@link #criteria(String, Predicate)} are defined
     * then it returns the first browser window that matches defined criteria. Otherwise
     * it returns most likely the first browser window/tab.
     *
     * @return an instance of {@link GetWindowSupplier}
     */
    @Description("Any (first found) browser window/tab")
    public static GetWindowSupplier window() {
        return new GetWindowSupplier(GetWindowSupplier::getListOfWindows)
                .from(currentContent());
    }

    /**
     * Creates an instance of {@link GetWindowSupplier} to get a browser window/tab
     *
     * @param index an index of the window/tab to get. Starts from 0.
     * @return an instance of {@link GetWindowSupplier}
     */
    @Description("Browser window/tab. Index {index}")
    public static GetWindowSupplier window(@DescriptionFragment("index") int index) {
        checkArgument(index >= 0, "Index should not be a negative value");
        return new GetWindowSupplier(webDriver -> ofNullable(getWindowByIndex(webDriver, index))
                .map(List::of)
                .orElseGet(List::of))
                .from(currentContent());
    }

    /**
     * Creates an instance of {@link GetWindowSupplier} to get current browser window/tab.
     * When {@link #criteria(Criteria)} or {@link #criteria(String, Predicate)} or {@link #criteriaOr(Criteria[])}
     * or {@link #criteriaOnlyOne(Criteria[])} ore {@link #criteriaNot(Criteria[])}are defined
     * then it returns current browser window/tab after it matches criteria. Otherwise it
     * returns current browser window/tab immediately.
     *
     * @return an instance of {@link GetWindowSupplier}
     */
    @Description("Current browser window/tab")
    public static GetWindowSupplier currentWindow() {
        return new GetWindowSupplier(webDriver -> of(new DefaultWindow(webDriver.getWindowHandle(), webDriver)))
                .from(currentContent());
    }

    public GetWindowSupplier title(String title) {
        return criteria(WindowCriteria.title(title));
    }

    public GetWindowSupplier titleMatches(String expression) {
        return criteria(WindowCriteria.titleMatches(expression));
    }

    public GetWindowSupplier pageAt(String url) {
        return criteria(WindowCriteria.pageAt(url));
    }

    public GetWindowSupplier pageAt(URL url) {
        return criteria(WindowCriteria.pageAt(url));
    }

    public GetWindowSupplier urlMatches(String expression) {
        return criteria(WindowCriteria.urlMatches(expression));
    }

    public GetWindowSupplier urlHost(String host) {
        return criteria(WindowCriteria.urlHost(host));
    }

    public GetWindowSupplier urlHostMatches(String expression) {
        return criteria(WindowCriteria.urlHostMatches(expression));
    }

    public GetWindowSupplier urlProtocol(String protocol) {
        return criteria(WindowCriteria.urlProtocol(protocol));
    }

    public GetWindowSupplier urlProtocolMatches(String expression) {
        return criteria(WindowCriteria.urlProtocolMatches(expression));
    }

    public GetWindowSupplier urlRef(String reference) {
        return criteria(WindowCriteria.urlRef(reference));
    }

    public GetWindowSupplier urlRefMatches(String expression) {
        return criteria(WindowCriteria.urlRefMatches(expression));
    }

    public GetWindowSupplier urlQuery(String query) {
        return criteria(WindowCriteria.urlQuery(query));
    }

    public GetWindowSupplier urlQueryMatches(String expression) {
        return criteria(WindowCriteria.urlQueryMatches(expression));
    }

    public GetWindowSupplier urlUserInfo(String userInfo) {
        return criteria(WindowCriteria.urlUserInfo(userInfo));
    }

    public GetWindowSupplier urlUserInfoMatches(String expression) {
        return criteria(WindowCriteria.urlUserInfoMatches(expression));
    }

    public GetWindowSupplier urlPath(String path) {
        return criteria(WindowCriteria.urlPath(path));
    }

    public GetWindowSupplier urlPathMatches(String expression) {
        return criteria(WindowCriteria.urlPathMatches(expression));
    }

    public GetWindowSupplier urlPort(int port) {
        return criteria(WindowCriteria.urlPort(port));
    }

    @Override
    public GetWindowSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
