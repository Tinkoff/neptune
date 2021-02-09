package ru.tinkoff.qa.neptune.selenium.functions.windows;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@SequentialGetStepSupplier.DefaultParameterNames(
        from = "Window/tab to get title of"
)
public final class GetWindowTitleSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, String, Window, GetWindowTitleSupplier> {

    private GetWindowTitleSupplier() {
        super("Title of the browser window/tab", Window::getTitle);
    }

    /**
     * Builds a function which gets title from the active window.
     *
     * @return Supplier of a function which gets title.
     */
    public static GetWindowTitleSupplier windowTitle() {
        return titleOf(currentWindow());
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
