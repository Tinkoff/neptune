package ru.tinkoff.qa.neptune.selenium.functions.windows;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@Description("Set window full screen")
@SequentialActionSupplier.DefinePerformOnParameterName("Window/tab to set fullscreen")
public final class FullScreenWindowSupplier extends SequentialActionSupplier<SeleniumStepContext, Window, FullScreenWindowSupplier> {

    private FullScreenWindowSupplier() {
        super();
    }

    /**
     * Builds an action which sets the active window full screen.
     *
     * @return Supplier of an action which sets the active window full screen
     */
    public static FullScreenWindowSupplier fullScreen() {
        return fullScreen(currentWindow());
    }

    /**
     * Builds an action which sets some window full screen.
     *
     * @param supplier is how to get the window to set it full screen
     * @return Supplier of an action sets a window full screen.
     */
    public static FullScreenWindowSupplier fullScreen(GetWindowSupplier supplier) {
        return new FullScreenWindowSupplier().performOn(supplier);
    }

    /**
     * Builds an action which sets the window full screen.
     *
     * @param window to set it full screen
     * @return Supplier of an action which sets the window full screen.
     */
    public static FullScreenWindowSupplier fullScreen(Window window) {
        return new FullScreenWindowSupplier().performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.fullscreen();
    }
}
