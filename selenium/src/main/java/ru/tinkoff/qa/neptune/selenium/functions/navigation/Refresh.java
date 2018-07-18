package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class Refresh extends NavigationActionSupplier<Refresh> {

    private Refresh() {
        super();
    }

    /**
     * Builds the refreshing in the first window.
     *
     * @return built the refreshing action
     */
    public static Refresh refresh() {
        return new Refresh().andThenRefresh();
    }

    /**
     * Builds the refreshing in some window which should be found.
     *
     * @param windowSupplier is how to get the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refresh(GetWindowSupplier windowSupplier) {
        return new Refresh().andThenRefresh(windowSupplier);
    }

    /**
     * Builds the refreshing in the window.
     *
     * @param window is the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refresh(Window window) {
        return new Refresh().andThenRefresh(window);
    }

    @Override
    protected void performActionOn(Window value, Object... ignored) {
        value.refresh();
    }

    /**
     * Adds another refreshing in the first window.
     *
     * @return built refreshing action
     */
    public Refresh andThenRefresh() {
        return andThenRefresh(window());
    }

    /**
     * Adds another refreshing in some window which should be found.
     *
     * @param windowSupplier is how to get the window where the refreshing should be performed
     * @return built refreshing action
     */
    public Refresh andThenRefresh(GetWindowSupplier windowSupplier) {
        return andThen("Refresh", windowSupplier);
    }

    /**
     * Adds another refreshing in the window.
     *
     * @param window is the window where the refreshing should be performed
     * @return built navigation action
     */
    public Refresh andThenRefresh(Window window) {
        return andThen("Refresh", window);
    }
}
