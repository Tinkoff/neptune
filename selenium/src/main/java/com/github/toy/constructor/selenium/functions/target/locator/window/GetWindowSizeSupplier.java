package com.github.toy.constructor.selenium.functions.target.locator.window;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class GetWindowSizeSupplier extends SequentialGetSupplier<SeleniumSteps, Dimension, Window, GetWindowSizeSupplier> {

    private GetWindowSizeSupplier() {
        super();
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

    @Override
    protected Function<Window, Dimension> getEndFunction() {
        return toGet("Size", WebDriver.Window::getSize);
    }
}
