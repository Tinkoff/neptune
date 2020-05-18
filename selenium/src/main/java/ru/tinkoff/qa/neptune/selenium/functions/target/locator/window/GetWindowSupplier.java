package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
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

    private GetWindowSupplier(Integer index) {
        super(buildDescription(index), driver -> ofNullable(index)
                .map(intIndex -> ofNullable(getWindowByIndex(driver, intIndex))
                        .map(List::of)
                        .orElseGet(List::of))
                .orElseGet(() -> getListOfWindows(driver)));
        timeOut(WAITING_WINDOW_TIME_DURATION.get());
        throwOnEmptyResult(() -> {
            String errorDescription = "Window/tab was not found";
            errorDescription = format("%s%s", errorDescription, ofNullable(index)
                    .map(integer -> format(". By index %s", integer)).orElse(EMPTY)).trim();

            var description = ofNullable(getCriteria())
                    .map(Criteria::toString)
                    .orElse(EMPTY);
            if (!isBlank(description)) {
                errorDescription = format("%s. Criteria:%s", errorDescription, description);
            }

            return new NoSuchWindowException(errorDescription);
        });
    }


    private static String buildDescription(Integer index) {
        return ofNullable(index)
                .map(integer -> format("Browser window/tab [index %s]", index))
                .orElse("Browser window/tab");
    }

    private static List<Window> getListOfWindows(WebDriver driver) {
        return driver.getWindowHandles()
                .stream().map(s -> new DefaultWindow(s, driver)).collect(Collectors.toList());
    }

    private static Window getWindowByIndex(WebDriver driver, int index) {
        var windows = getListOfWindows(driver);
        if (windows.size() >= index + 1) {
            return windows.get(index);
        }
        return null;
    }

    /**
     * Creates an instance of {@link GetWindowSupplier} to get a browser window/tab
     *
     * @return an instance of {@link GetWindowSupplier}
     */
    public static GetWindowSupplier window() {
        return window(null);
    }

    /**
     * Creates an instance of {@link GetWindowSupplier} to get a browser window/tab
     *
     * @param index an index of the window/tab to get. Starts from 0.
     * @return an instance of {@link GetWindowSupplier}
     */
    public static GetWindowSupplier window(Integer index) {
        checkArgument(ofNullable(index)
                .map(integer -> integer >= 0)
                .orElse(true), "Index should not be a negative value");
        return new GetWindowSupplier(index).from(currentContent());
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
