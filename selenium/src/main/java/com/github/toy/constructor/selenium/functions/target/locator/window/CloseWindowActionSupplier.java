package com.github.toy.constructor.selenium.functions.target.locator.window;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class CloseWindowActionSupplier extends SequentialActionSupplier<SeleniumSteps, Window, CloseWindowActionSupplier> {

    private CloseWindowActionSupplier() {
        super();
    }

    /**
     * Builds an action which closes the first window.
     *
     * @return Supplier of an action which closes the first window.
     */
    public static CloseWindowActionSupplier closeWindow() {
        return closeWindow(window());
    }

    /**
     * Builds an action which closes some window.
     *
     * @param supplier is how to get the window to close
     * @return Supplier of an action which closes the window.
     */
    public static CloseWindowActionSupplier closeWindow(GetWindowSupplier supplier) {
        return new CloseWindowActionSupplier().andThenCloseWindow(supplier);
    }

    /**
     * Builds an action which closes some window.
     *
     * @param window the window to close
     * @return Supplier of an action which closes the window.
     */
    public static CloseWindowActionSupplier closeWindow(Window window) {
        return new CloseWindowActionSupplier().andThenCloseWindow(window);
    }

    /**
     * Adds another action which closes some window.
     *
     * @param supplier is how to get the window to close
     * @return self-reference
     */
    public CloseWindowActionSupplier andThenCloseWindow(GetWindowSupplier supplier) {
        return andThen("Close window/tab", supplier);
    }

    /**
     * Adds another action which closes the window.
     *
     * @param window to close
     * @return self-reference
     */
    public CloseWindowActionSupplier andThenCloseWindow(Window window) {
        return andThen("Close window/tab", window);
    }

    @Override
    protected void performActionOn(Window value, Object... ignored) {
        value.close();
    }
}
