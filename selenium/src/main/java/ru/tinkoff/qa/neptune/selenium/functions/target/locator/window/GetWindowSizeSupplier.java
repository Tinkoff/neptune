package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class GetWindowSizeSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, Dimension, Window, GetWindowSizeSupplier> {

    private GetWindowSizeSupplier() {
        super("Size of the window", WebDriver.Window::getSize);
    }

    /**
     * Builds a function which gets size of the first window.
     *
     * @return Supplier of a function which gets size.
     */
    public static GetWindowSizeSupplier windowSize() {
        return sizeOf(window());
    }

    /**
     * Builds a function which gets size of some window.
     *
     * @param supplier is how to get the window which should return size
     * @return Supplier of a function which gets size.
     */
    public static GetWindowSizeSupplier sizeOf(GetWindowSupplier supplier) {
        return new GetWindowSizeSupplier().from(supplier);
    }

    /**
     * Builds a function which gets size of the given window.
     *
     * @param window to get size of
     * @return Supplier of a function which gets size.
     */
    public static GetWindowSizeSupplier sizeOf(Window window) {
        return new GetWindowSizeSupplier().from(window);
    }
}
