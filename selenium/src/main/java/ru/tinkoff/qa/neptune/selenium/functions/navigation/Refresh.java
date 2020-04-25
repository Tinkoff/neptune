package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public final class Refresh extends SequentialActionSupplier<SeleniumStepContext, Window, Refresh> {


    private Refresh() {
        super("Refresh window/tab");
    }

    /**
     * Builds the refreshing in the browser window/tab that active currently.
     *
     * @return built the refreshing action
     */
    public static Refresh refreshWindow() {
        return refreshWindow(window());
    }

    /**
     * Builds the refreshing in some window which should be found.
     *
     * @param windowSupplier is how to get the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refreshWindow(GetWindowSupplier windowSupplier) {
        return new Refresh().performOn(windowSupplier);
    }

    /**
     * Builds the refreshing in the window.
     *
     * @param window is the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refreshWindow(Window window) {
        return new Refresh().performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.refresh();
    }
}
