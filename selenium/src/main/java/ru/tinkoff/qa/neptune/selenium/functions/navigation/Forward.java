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
public final class Forward extends SequentialActionSupplier<SeleniumStepContext, Window, Forward> {

    private Forward() {
        super("Navigate forward");
    }

    /**
     * Builds navigation forward in the first window.
     *
     * @return built navigation action
     */
    public static Forward forward() {
        return forward(window());
    }

    /**
     * Builds navigation forward in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static Forward forward(GetWindowSupplier windowSupplier) {
        return new Forward().performOn(windowSupplier);
    }

    /**
     * Builds navigation forward in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Forward forward(Window window) {
        return new Forward().performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.forward();
    }
}
