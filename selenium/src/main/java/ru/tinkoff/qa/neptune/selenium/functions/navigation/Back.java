package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class Back extends NavigationActionSupplier<Back> {

    private Back(String description) {
        super(description);
    }

    /**
     * Builds navigation back in the first window.
     *
     * @return built navigation action
     */
    public static Back back() {
        return back(window());
    }

    /**
     * Builds navigation back in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(GetWindowSupplier windowSupplier) {
        return new Back("Navigate back").performOn(windowSupplier);
    }

    /**
     * Builds navigation back in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(Window window) {
        return new Back("Navigate back").performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.back();
    }
}
