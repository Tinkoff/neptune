package com.github.toy.constructor.selenium.functions.target.locator.window;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetObjectFromIterable.getFromIterable;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingle;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingleOnCondition;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.WAITING_WINDOW_TIME_DURATION;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class GetWindowSupplier extends GetSupplier<SeleniumSteps, Window, GetWindowSupplier>
        implements TargetLocatorSupplier<Window> {

    private Predicate<Window> condition;
    private Duration timeOut = WAITING_WINDOW_TIME_DURATION.get();
    private Integer index;

    private GetWindowSupplier() {
        super();
    }

    private static List<Window> getListOfWindows(SeleniumSteps seleniumSteps, String description) {
        WebDriver driver = seleniumSteps.getWrappedDriver();
        return driver.getWindowHandles()
                .stream().map(s -> {
                    DefaultWindow window = new DefaultWindow(s, driver);
                    window.setDescription(description);
                    return window;
                }).collect(Collectors.toList());
    }

    private static Window getWindowByIndex(SeleniumSteps seleniumSteps, String description, int index) {
        List<Window> windows = getListOfWindows(seleniumSteps, description);
        if (windows.size() >= index + 1) {
            return windows.get(index);
        }
        return null;
    }

    private static Supplier<NoSuchWindowException> noSuchWindowException(String message) {
        return () -> new NoSuchWindowException(message);
    }

    /**
     * Creates an instance of {@link GetSupplier}
     *
     * @return reference to a new instance of {@link GetSupplier}
     */
    public static GetWindowSupplier window() {
        return new GetWindowSupplier();
    }

    /**
     * Sets the index of required window to get.
     *
     * @param index of required window.
     * @return self-reference.
     */
    public GetWindowSupplier byIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Adds a criteria to find the desired window.
     *
     * @param condition criteria to be used to find the desired window.
     * @return self-reference.
     */
    public GetWindowSupplier onCondition(Predicate<Window> condition) {
        checkArgument(condition != null, "Condition is not defined");
        this.condition = ofNullable(this.condition).map(predicate -> this.condition.and(predicate)).orElse(condition);
        return this;
    }

    /**
     * Sets the time to get desired window. If this time has no been set up and {@link #byIndex(int)} or/and
     * {@link #onCondition(Predicate)} had been invoked then the searching for the window takes time defined at
     * {@link com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_WINDOW_TIME_DURATION}.
     *
     * @param timeOut time of the searching for the desired window/tab.
     * @return self-reference.
     */
    public GetWindowSupplier withTimeToGetWindow(Duration timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    @Override
    public Function<SeleniumSteps, Window> get() {
        return ofNullable(super.get()).orElseGet(() -> {
            if (condition == null && index == null) {
                set(toGet("The first window/tab", seleniumSteps ->
                        getWindowByIndex(seleniumSteps, "The first window", 0)));
                return super.get();
            }

            Function<SeleniumSteps, Window> result = ofNullable(index).map(integer -> ofNullable(this.condition).map(windowPredicate ->
                    getSingleOnCondition("Window/tab",
                            toGet(format("Window/tab by index %s", integer),
                                    (Function<SeleniumSteps, Window>) seleniumSteps -> getWindowByIndex(seleniumSteps,
                                            format("Window/tab found by index %s on condition '%s'", integer, windowPredicate), integer)),
                            windowPredicate,
                            timeOut, true,
                            noSuchWindowException(format("Window/tab was not found by index %s on condition '%s'", integer, windowPredicate))))

                    .orElseGet(() -> getSingle(toGet(format("Window/tab by index %s", integer),
                            (Function<SeleniumSteps, Window>) seleniumSteps -> getWindowByIndex(seleniumSteps,
                                    format("Window/tab found by index %s", integer), integer)),
                            timeOut,
                            noSuchWindowException(format("Window/tab was not found by index %s", index)))))

                    .orElseGet(() ->
                            ofNullable(this.condition).map(windowPredicate ->
                                    getFromIterable("Window/tab",
                                            toGet("Windows/tabs which are currently present",
                                                    (Function<SeleniumSteps, List<Window>>) seleniumSteps ->
                                                            getListOfWindows(seleniumSteps, format("Window/tab found on condition '%s'", windowPredicate))),
                                            windowPredicate,
                                            timeOut,
                                            false, true,
                                            noSuchWindowException(format("Window was not found on condition '%s'",  windowPredicate))))

                                    .orElseGet(() -> toGet("The first window/tab",
                                            seleniumSteps -> getWindowByIndex(seleniumSteps, "The first window", 0))));

            set(result);
            return result;
        });
    }
}
