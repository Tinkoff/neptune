package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class Forward extends NavigationActionSupplier<Forward> {

    private Forward() {
        super();
    }

    /**
     * Builds navigation forward in the first window.
     *
     * @return built navigation action
     */
    public static Forward forward() {
        return new Forward().andThenForward();
    }

    /**
     * Builds navigation forward in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static Forward forward(GetWindowSupplier windowSupplier) {
        return new Forward().andThenForward(windowSupplier);
    }

    /**
     * Builds navigation forward in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Forward forward(Window window) {
        return new Forward().andThenForward(window);
    }

    /**
     * Adds another navigation forward in the first window.
     *
     * @return built navigation action
     */
    public Forward andThenForward() {
        return andThenForward(window());
    }

    /**
     * Adds another navigation forward in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public Forward andThenForward(GetWindowSupplier windowSupplier) {
        return andThen("Navigate forward", windowSupplier);
    }

    /**
     * Adds another navigation forward in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public Forward andThenForward(Window window) {
        return andThen("Navigate forward", window);
    }

    @Override
    protected void performActionOn(Window value, Object... ignored) {
        value.forward();
    }
}
