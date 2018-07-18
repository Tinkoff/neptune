package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import org.openqa.selenium.Dimension;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class SetWindowSizeSupplier extends SequentialActionSupplier<SeleniumSteps, Window, SetWindowSizeSupplier> {

    private SetWindowSizeSupplier() {
        super();
    }

    /**
     * Builds an action which changes size of the first window.
     *
     * @param size is the new size of the window
     * @return Supplier of an action which changes size of the first window.
     */
    public static SetWindowSizeSupplier setWindowSize(Dimension size) {
        return setSizeOf(window(), size);
    }

    /**
     * Builds an action which changes size of some window.
     *
     * @param supplier is how to get the window to change size
     * @param size is the new size of the window
     * @return Supplier of an action which changes window size.
     */
    public static SetWindowSizeSupplier setSizeOf(GetWindowSupplier supplier, Dimension size) {
        return new SetWindowSizeSupplier().andThen("Set size of the window", supplier, size);
    }

    /**
     * Builds an action which changes size of the window.
     *
     * @param window to change size of
     * @param size is the new size of the window
     * @return Supplier of an action which changes window size.
     */
    public static SetWindowSizeSupplier setSizeOf(Window window, Dimension size) {
        return new SetWindowSizeSupplier().andThen("Set size of the window", window, size);
    }

    /**
     * Adds another action which changes size of some window.
     *
     * @param supplier is how to get the window to change size
     * @param size is the new size of the window
     * @return self-reference
     */
    public SetWindowSizeSupplier andSetSizeOf(GetWindowSupplier supplier, Dimension size) {
        return andThen("Set size of the window", supplier, size);
    }

    /**
     * Adds another action which changes size of the window.
     *
     * @param window to change size of
     * @param size is the new size of the window
     * @return self-reference
     */
    public SetWindowSizeSupplier andSetSizeOf(Window window, Dimension size) {
        return andThen("Set size of the window", window, size);
    }

    @Override
    protected void performActionOn(Window value, Object... additionalArgument) {
        value.setSize(Dimension.class.cast(additionalArgument[0]));
    }
}
