package ru.tinkoff.qa.neptune.selenium.functions.windows;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Window/tab to close"
)
public final class CloseWindowActionSupplier extends SequentialActionSupplier<SeleniumStepContext, Window, CloseWindowActionSupplier> {

    private CloseWindowActionSupplier() {
        super(/*"Close the browser window/tab"*/);
    }

    /**
     * Builds an action which closes the active window.
     *
     * @return Supplier of an action which closes the active window.
     */
    public static CloseWindowActionSupplier closeWindow() {
        return closeWindow(currentWindow());
    }

    /**
     * Builds an action which closes some window.
     *
     * @param supplier is how to get the window to close
     * @return Supplier of an action which closes the window.
     */
    public static CloseWindowActionSupplier closeWindow(GetWindowSupplier supplier) {
        return new CloseWindowActionSupplier().performOn(supplier);
    }

    /**
     * Builds an action which closes some window.
     *
     * @param window the window to close
     * @return Supplier of an action which closes the window.
     */
    public static CloseWindowActionSupplier closeWindow(Window window) {
        return new CloseWindowActionSupplier().performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.close();
    }
}
