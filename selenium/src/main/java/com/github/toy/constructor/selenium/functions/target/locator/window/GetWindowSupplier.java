package com.github.toy.constructor.selenium.functions.target.locator.window;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetObjectFromIterable.getFromIterable;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingle;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingleOnCondition;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasUrl;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.WAITING_WINDOW_TIME_DURATION;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class GetWindowSupplier extends GetSupplier<SeleniumSteps, Window, GetWindowSupplier> {

    private static final Function<SeleniumSteps, List<Window>> GET_WINDOWS =
            toGet("Browser/web windows/tabs", seleniumSteps -> {
                WebDriver driver = seleniumSteps.getWrappedDriver();
                return driver.getWindowHandles()
                        .stream().map(s -> new DefaultWindow(s, driver)).collect(Collectors.toList());
            });

    private Predicate<Window> condition;
    private Duration timeOut = WAITING_WINDOW_TIME_DURATION.get();
    private Integer index;

    private GetWindowSupplier() {
        super();
    }

    private static Function<SeleniumSteps, Window> getWindowByIndex(int index) {
        checkArgument(index >= 0, "Index of the window should be greater or equal 0");
        return toGet(format("Browser/web window/tab by index %s", index), seleniumSteps -> {
            WebDriver driver = seleniumSteps.getWrappedDriver();
            List<String> handles = new ArrayList<>(driver.getWindowHandles());
            if (handles.size() >= index + 1) {
                return new DefaultWindow(handles.get(index), driver);
            }
            return null;
        });
    }

    private static Supplier<NoSuchWindowException> noSuchWindowException(String message) {
        return () -> new NoSuchWindowException(message);
    }

    public static GetWindowSupplier window() {
        return new GetWindowSupplier();
    }

    public GetWindowSupplier byIndex(int index) {
        this.index = index;
        return this;
    }

    public GetWindowSupplier onCondition(Predicate<Window> condition) {
        checkArgument(condition != null, "Condition is not defined");
        this.condition = ofNullable(condition).map(predicate -> this.condition.and(predicate)).orElse(condition);
        return this;
    }

    public GetWindowSupplier withTitle(String title) {
        return onCondition(hasTitle(title));
    }

    public GetWindowSupplier withTitle(Pattern titlePattern) {
        return onCondition(hasTitle(titlePattern));
    }

    public GetWindowSupplier withUrl(URL url) {
        return onCondition(hasUrl(url));
    }

    public GetWindowSupplier withUrl(Pattern urlPattern) {
        return onCondition(hasUrl(urlPattern));
    }

    public GetWindowSupplier withTimeOut(Duration timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    @Override
    public Function<SeleniumSteps, Window> get() {
        return ofNullable(super.get()).orElseGet(() -> {
            if (condition == null && index == null) {
                set(getWindowByIndex(0));
                return super.get();
            }

            Function<SeleniumSteps, Window> result;
            if (index != null) {
                result = ofNullable(condition).map(windowPredicate ->
                        getSingleOnCondition("Window", getWindowByIndex(index), condition,
                                timeOut, true,
                                noSuchWindowException(format("Window was not found by index %s on condition '%s'", index, condition))))

                        .orElseGet(() -> getSingle(getWindowByIndex(index),
                                timeOut,
                                noSuchWindowException(format("Window was not found by index %s on condition '%s'", index, condition))));
            }
            else {
                result = ofNullable(condition).map(windowPredicate ->
                        getFromIterable("Window", GET_WINDOWS, condition, timeOut, false, true,
                                noSuchWindowException(format("Window was not found on condition '%s'", index, condition))))

                        .orElseGet(() -> getWindowByIndex(0));
            }

            set(result);
            return result;
        });
    }
}
