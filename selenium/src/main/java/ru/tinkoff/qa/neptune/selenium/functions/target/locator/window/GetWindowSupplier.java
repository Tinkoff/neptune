package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

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

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialGetStepSupplier.DefaultParameterNames(
        timeOut = "Time of the waiting for the browser window/tab",
        criteria = "Window criteria"
)
public final class GetWindowSupplier extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<SeleniumStepContext, Window, WebDriver, GetWindowSupplier>
        implements TargetLocatorSupplier<Window> {

    @StepParameter(value = "Window number/index", doNotReportNullValues = true)
    private final Integer index;

    private GetWindowSupplier(Function<WebDriver, List<Window>> function, Integer index) {
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
        throwOnEmptyResult(() -> new NoSuchWindowException("Window/tab was not found"));
        this.index = index;
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
    @Description("Browser window/tab")
    public static GetWindowSupplier window() {
        return new GetWindowSupplier(GetWindowSupplier::getListOfWindows,
                null)
                .from(currentContent());
    }

    /**
     * Creates an instance of {@link GetWindowSupplier} to get a browser window/tab
     *
     * @param index an index of the window/tab to get. Starts from 0.
     * @return an instance of {@link GetWindowSupplier}
     */
    @Description("Browser window/tab")
    public static GetWindowSupplier window(int index) {
        checkArgument(index >= 0, "Index should not be a negative value");
        return new GetWindowSupplier(webDriver -> ofNullable(getWindowByIndex(webDriver, index))
                .map(List::of)
                .orElseGet(List::of), index)
                .from(currentContent());
    }

    /**
     * Creates an instance of {@link GetWindowSupplier} to get current browser window/tab.
     * When {@link #criteria(Criteria)} or {@link #criteria(String, Predicate)} are defined
     * then it returns current browser window/tab after it matches criteria. Otherwise it
     * returns current browser window/tab immediately.
     *
     * @return an instance of {@link GetWindowSupplier}
     */
    @Description("Current browser window/tab")
    public static GetWindowSupplier currentWindow() {
        return new GetWindowSupplier(webDriver -> of(new DefaultWindow(webDriver.getWindowHandle(), webDriver)), null)
                .from(currentContent());
    }

    @Override
    public GetWindowSupplier criteria(Criteria<? super Window> condition) {
        return super.criteria(condition);
    }

    @Override
    public GetWindowSupplier criteria(String description, Predicate<? super Window> condition) {
        return super.criteria(description, condition);
    }

    @Override
    public GetWindowSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
