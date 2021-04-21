package ru.tinkoff.qa.neptune.selenium.functions.windows;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@SequentialGetStepSupplier.DefineFromParameterName("Window/tab to get size of")
@Description("Size of the browser window/tab")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class GetWindowSizeSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, Dimension, Window, GetWindowSizeSupplier> {

    private GetWindowSizeSupplier() {
        super(WebDriver.Window::getSize);
    }

    /**
     * Builds a function which gets size of the active window.
     *
     * @return Supplier of a function which gets size.
     */
    public static GetWindowSizeSupplier windowSize() {
        return windowSize(currentWindow());
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
