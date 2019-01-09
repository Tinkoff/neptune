package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetObjectFromIterable.getFromIterable;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_WINDOW_TIME_DURATION;
import static com.google.common.base.Preconditions.checkArgument;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@MakeImageCapturesOnFinishing
public final class GetWindowSupplier extends GetStepSupplier<SeleniumSteps, Window, GetWindowSupplier>
        implements TargetLocatorSupplier<Window> {

    private Predicate<Window> condition;
    private Duration timeOut = WAITING_WINDOW_TIME_DURATION.get();
    private Integer index;

    private GetWindowSupplier() {
        super();
    }

    private static List<Window> getListOfWindows(WebDriver driver) {
        return driver.getWindowHandles()
                .stream().map(s -> new DefaultWindow(s, driver)).collect(Collectors.toList());
    }

    private static Window getWindowByIndex(WebDriver driver, int index) {
        var windows = getListOfWindows(driver);
        if (windows.size() >= index + 1) {
            var result = windows.get(index);
            result.switchToMe();
            return result;
        }
        return null;
    }

    private static Supplier<NoSuchWindowException> noSuchWindowException(String message) {
        return () -> new NoSuchWindowException(message);
    }

    /**
     * Creates an instance of {@link GetStepSupplier}
     *
     * @return reference to a new instance of {@link GetStepSupplier}
     */
    public static GetWindowSupplier window() {
        return new GetWindowSupplier().set(toGet("The first window/tab",
                currentContent().andThen(webDriver -> getWindowByIndex(webDriver, 0))));
    }

    private GetWindowSupplier setFunctionWithIndexAndCondition() {
        return set(getSingle(format("Window/tab by index %s", index),
                currentContent().andThen(webDriver -> getWindowByIndex(webDriver, index)),
                condition,
                timeOut, true,
                noSuchWindowException(format("Window/tab was not found by index %s and by conditions %s", index, condition))));
    }

    private GetWindowSupplier setFunctionWithCondition() {
        return set(getFromIterable("Window/tab",
                currentContent().andThen(GetWindowSupplier::getListOfWindows),
                condition, timeOut,
                false, true,
                noSuchWindowException(format("Window was not found by conditions %s",  condition))));
    }

    private GetWindowSupplier setFunctionWithIndex() {
        return set(getSingle(format("Window/tab by index %s", index),
                currentContent().andThen(webDriver -> getWindowByIndex(webDriver, index)),
                timeOut,
                noSuchWindowException(format("Window/tab was not found by index %s", index))));
    }

    /**
     * Sets the index of required window to get.
     *
     * @param index of required window.
     * @return self-reference.
     */
    public GetWindowSupplier byIndex(int index) {
        checkArgument(index >= 0, "Index of a window/tab should be greater than zero");
        this.index = index;
        return ofNullable(condition).map(windowPredicate -> setFunctionWithIndexAndCondition())
                .orElseGet(this::setFunctionWithIndex);
    }

    /**
     * Adds a criteria to find the desired window.
     *
     * @param condition criteria to be used to find the desired window.
     * @return self-reference.
     */
    public GetWindowSupplier onCondition(Predicate<Window> condition) {
        checkArgument(nonNull(condition), "Condition is not defined");
        this.condition = ofNullable(this.condition).map(predicate -> this.condition.and(predicate)).orElse(condition);
        return ofNullable(index).map(integer -> setFunctionWithIndexAndCondition())
                .orElseGet(this::setFunctionWithCondition);

    }

    /**
     * Sets the time to get desired window. If this time has no been set up and {@link #byIndex(int)} or/and
     * {@link #onCondition(Predicate)} had been invoked then the searching for the window takes time defined at
     * {@link ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties#WAITING_WINDOW_TIME_DURATION}.
     *
     * @param timeOut time of the searching for the desired window/tab.
     * @return self-reference.
     */
    public GetWindowSupplier withTimeToGetWindow(Duration timeOut) {
        this.timeOut = timeOut;
        return ofNullable(index).map(integer ->
                ofNullable(condition)
                        .map(windowPredicate -> setFunctionWithIndexAndCondition())
                        .orElseGet(this::setFunctionWithIndex))

                .orElseGet(() -> ofNullable(condition)
                        .map(windowPredicate -> setFunctionWithCondition())
                        .orElseGet(() -> set(toGet("The first window/tab",
                                currentContent().andThen(webDriver ->
                                        getWindowByIndex(webDriver, 0))))));
    }
}
