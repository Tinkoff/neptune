package ru.tinkoff.qa.neptune.selenium.functions.windows;

import org.openqa.selenium.Dimension;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
@SequentialActionSupplier.DefinePerformOnParameterName("Window/tab to change size of")
@Description("Set new size {newSize} of the browser window/tab")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class SetWindowSizeSupplier extends SequentialActionSupplier<SeleniumStepContext, Window, SetWindowSizeSupplier> {

    @DescriptionFragment("newSize")
    private final Dimension size;

    private SetWindowSizeSupplier(Dimension size) {
        super();
        this.size = size;
    }

    /**
     * Builds an action which changes size of the active window.
     *
     * @param size is the new size of the window
     * @return Supplier of an action which changes size of the active window.
     */
    public static SetWindowSizeSupplier setWindowSize(Dimension size) {
        return setSizeOf(currentWindow(), size);
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
    protected void howToPerform(Window value) {
        value.setSize(size);
    }
}
