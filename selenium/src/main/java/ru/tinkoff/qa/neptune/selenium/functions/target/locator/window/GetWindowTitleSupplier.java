package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class GetWindowTitleSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, String, Window, GetWindowTitleSupplier> {

    private GetWindowTitleSupplier() {
        super("Title of the window", Window::getTitle);
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
}
