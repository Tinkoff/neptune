package com.github.toy.constructor.selenium.functions.navigation;

import com.github.toy.constructor.core.api.SequentialGetStepSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier;
import com.github.toy.constructor.selenium.functions.target.locator.window.Window;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class GetCurrentUrlSupplier extends SequentialGetStepSupplier<SeleniumSteps, String, Window, GetCurrentUrlSupplier> {

    private GetCurrentUrlSupplier() {
        super();
    }

    /**
     * Builds a function which returns url of the page loaded in the first window/tab.
     *
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the current window/tab.
     */
    public static GetCurrentUrlSupplier currentUrl() {
        return new GetCurrentUrlSupplier().from(window());
    }

    /**
     * Builds a function which returns url of the page loaded in some window/tab. This window/tab is supposed to be found.
     *
     * @param from is how to find the window where loaded url should be taken
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in some window/tab which should be got by criteria.
     */
    public static GetCurrentUrlSupplier currentUrlIn(GetWindowSupplier from) {
        return new GetCurrentUrlSupplier().from(from);
    }

    /**
     * Builds a function which returns url of the page loaded in the window/tab.
     * @param from is the window where loaded url should be taken
     * @return n instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the window/tab.
     */
    public static GetCurrentUrlSupplier currentUrlIn(Window from) {
        return new GetCurrentUrlSupplier().from(from);
    }

    @Override
    protected Function<Window, String> getEndFunction() {
        return toGet("URL of the loaded page", Window::getCurrentUrl);
    }
}
