package ru.tinkoff.qa.neptune.selenium.functions.windows;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

@SequentialGetStepSupplier.DefaultParameterNames(
        from = "Window/tab to get size of"
)
public final class GetWindowSizeSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, Dimension, Window, GetWindowSizeSupplier> {

    private GetWindowSizeSupplier() {
        super("Size of the browser window/tab", WebDriver.Window::getSize);
    }

    /**
     * Builds a function which gets size of the first window.
     *
     * @return Supplier of a function which gets size.
     */
    public static GetWindowSizeSupplier windowSize() {
        return windowSize(window());
    }

    /**
     * Builds a function which gets size of some window.
     *
     * @param supplier is how to get the window which should return size
     * @return Supplier of a function which gets size.
     */
    public static GetWindowSizeSupplier windowSize(GetWindowSupplier supplier) {
        return new GetWindowSizeSupplier().from(supplier);
    }

    /**
     * Builds a function which gets size of the given window.
     *
     * @param window to get size of
     * @return Supplier of a function which gets size.
     */
    public static GetWindowSizeSupplier windowSize(Window window) {
        return new GetWindowSizeSupplier().from(window);
    }
}
