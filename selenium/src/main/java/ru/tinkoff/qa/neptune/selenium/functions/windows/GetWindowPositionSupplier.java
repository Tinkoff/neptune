package ru.tinkoff.qa.neptune.selenium.functions.windows;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@SequentialGetStepSupplier.DefaultParameterNames(
        from = "Window/tab to get position of"
)
public final class GetWindowPositionSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, Point, Window, GetWindowPositionSupplier> {

    private GetWindowPositionSupplier() {
        super("Position of the browser window/tab", WebDriver.Window::getPosition);
    }

    /**
     * Builds a function which gets position of the active window.
     *
     * @return Supplier of a function which gets position.
     */
    public static GetWindowPositionSupplier windowPosition() {
        return positionOf(currentWindow());
    }

    /**
     * Builds a function which gets position of some window.
     *
     * @param supplier is how to get the window which should return position
     * @return Supplier of a function which gets position.
     */
    public static GetWindowPositionSupplier positionOf(GetWindowSupplier supplier) {
        return new GetWindowPositionSupplier().from(supplier);
    }

    /**
     * Builds a function which gets position of the given window.
     *
     * @param window to get position of
     * @return Supplier of a function which gets position.
     */
    public static GetWindowPositionSupplier positionOf(Window window) {
        return new GetWindowPositionSupplier().from(window);
    }
}
