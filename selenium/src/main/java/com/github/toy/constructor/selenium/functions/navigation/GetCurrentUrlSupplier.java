package com.github.toy.constructor.selenium.functions.navigation;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.window.Window;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;

public final class GetCurrentUrlSupplier extends SequentialGetSupplier<SeleniumSteps, String, WebDriver, GetCurrentUrlSupplier> {

    private GetCurrentUrlSupplier() {
        super();
    }

    /**
     * Builds a function which returns url of the page loaded in the current window/tab.
     *
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the current window/tab.
     */
    public static GetCurrentUrlSupplier currentUrl() {
        return new GetCurrentUrlSupplier().from(toGet("Current window", SeleniumSteps::getWrappedDriver));
    }

    /**
     * Builds a function which returns url of the page loaded in some window/tab. This window/tab is supposed to be found.
     *
     * @param from is how to find the window where loaded url should be taken
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in some window/tab which should be got by criteria.
     */
    public static GetCurrentUrlSupplier currentUrlIn(GetWindowSupplier from) {
        checkArgument(from != null, "The way how to get window should be defined");
        return new GetCurrentUrlSupplier().from(toGet(String.format("Window %s", from.get()),
                seleniumSteps -> seleniumSteps.get(from).getWrappedDriver()));
    }

    /**
     * Builds a function which returns url of the page loaded in the window/tab.
     * @param from is the window where loaded url should be taken
     * @return n instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the window/tab.
     */
    public static GetCurrentUrlSupplier currentUrlIn(Window from) {
        checkArgument(from != null, "Window should be defined");
        return new GetCurrentUrlSupplier().from(toGet(String.format("Window %s", from), seleniumSteps -> {
            from.switchToMe();
            return from.getWrappedDriver();
        }));
    }

    @Override
    protected Function<WebDriver, String> getEndFunction() {
        return toGet("Current URL", WebDriver::getCurrentUrl);
    }
}
