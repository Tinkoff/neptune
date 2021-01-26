package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Window/tab to perform navigation back"
)
public final class Back extends SequentialActionSupplier<SeleniumStepContext, Window, Back> {

    private Back() {
        super("Navigate back");
    }

    /**
     * Builds navigation back in the browser window/tab that active currently.
     *
     * @return built navigation action
     */
    public static Back back() {
        return back(currentWindow());
    }

    /**
     * Builds navigation back in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(GetWindowSupplier windowSupplier) {
        return new Back().performOn(windowSupplier);
    }

    /**
     * Builds navigation back in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(Window window) {
        return new Back().performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.back();
    }
}
