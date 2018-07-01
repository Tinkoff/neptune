package com.github.toy.constructor.selenium.functions.target.locator.window;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class GetWindowTitleSupplier extends SequentialGetSupplier<SeleniumSteps, String, Window, GetWindowTitleSupplier> {

    private GetWindowTitleSupplier() {
        super();
    }

    /**
     * Builds a function which gets title from the first window.
     *
     * @return Supplier of a function which gets title.
     */
    public static GetWindowTitleSupplier windowTitle() {
        return titleOf(window());
    }

    /**
     * Builds a function which gets title from some window.
     *
     * @param supplier is how to get the window which should return title
     * @return Supplier of a function which gets title.
     */
    public static GetWindowTitleSupplier titleOf(GetWindowSupplier supplier) {
        return new GetWindowTitleSupplier().from(supplier);
    }

    /**
     * Builds a function which gets title from the given window.
     *
     * @param window to get title from
     * @return Supplier of a function which gets title.
     */
    public static GetWindowTitleSupplier titleOf(Window window) {
        return new GetWindowTitleSupplier().from(window);
    }

    @Override
    protected Function<Window, String> getEndFunction() {
        return toGet("Title of the window", Window::getTitle);
    }
}
