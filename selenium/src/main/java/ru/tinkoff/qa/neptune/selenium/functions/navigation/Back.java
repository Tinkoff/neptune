package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class Back extends NavigationActionSupplier<Back> {

    private Back() {
        super("Navigate back");
    }

    /**
     * Builds navigation back in the first window.
     *
     * @return built navigation action
     */
    public static Back back() {
        return new Back().andThenBack();
    }

    /**
     * Builds navigation back in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(GetWindowSupplier windowSupplier) {
        return new Back().andThenBack(windowSupplier);
    }

    /**
     * Builds navigation back in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(Window window) {
        return new Back().andThenBack(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.back();
    }

    /**
     * Adds another navigation back in the first window.
     *
     * @return built navigation action
     */
    public Back andThenBack() {
        return andThenBack(window());
    }

    /**
     * Adds another navigation back in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public Back andThenBack(GetWindowSupplier windowSupplier) {
        return performOn(windowSupplier);
    }

    /**
     * Adds another navigation back in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public Back andThenBack(Window window) {
        return performOn(window);
    }
}
