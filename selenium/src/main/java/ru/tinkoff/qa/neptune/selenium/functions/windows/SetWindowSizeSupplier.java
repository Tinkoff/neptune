package ru.tinkoff.qa.neptune.selenium.functions.windows;

import org.openqa.selenium.Dimension;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public final class SetWindowSizeSupplier extends SequentialActionSupplier<SeleniumStepContext, Window, SetWindowSizeSupplier> {

    @StepParameter("New Size")
    private final Dimension size;

    private SetWindowSizeSupplier(Dimension size) {
        super("Change size of the browser window/tab");
        this.size = size;
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
     * @param size     is the new size of the window
     * @return Supplier of an action which changes window size.
     */
    public static SetWindowSizeSupplier setSizeOf(GetWindowSupplier supplier, Dimension size) {
        return new SetWindowSizeSupplier(size).performOn(supplier);
    }

    /**
     * Builds an action which changes size of the window.
     *
     * @param window to change size of
     * @param size   is the new size of the window
     * @return Supplier of an action which changes window size.
     */
    public static SetWindowSizeSupplier setSizeOf(Window window, Dimension size) {
        return new SetWindowSizeSupplier(size).performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.setSize(size);
    }
}
