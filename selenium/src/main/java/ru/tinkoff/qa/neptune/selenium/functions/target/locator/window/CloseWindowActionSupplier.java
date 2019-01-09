package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class CloseWindowActionSupplier extends SequentialActionSupplier<SeleniumSteps, Window, CloseWindowActionSupplier> {

    private CloseWindowActionSupplier(String description) {
        super(format("Close %s", description));
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
        return new CloseWindowActionSupplier(supplier.toString()).performOn(supplier);
    }

    /**
     * Builds an action which closes some window.
     *
     * @param window the window to close
     * @return Supplier of an action which closes the window.
     */
    public static CloseWindowActionSupplier closeWindow(Window window) {
        return new CloseWindowActionSupplier(window.toString()).performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.close();
    }
}
