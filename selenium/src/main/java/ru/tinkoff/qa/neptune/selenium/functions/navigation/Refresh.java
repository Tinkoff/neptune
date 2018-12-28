package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class Refresh extends NavigationActionSupplier<Refresh> {

    private static final String DESCRIPTION = "Refresh %s";

    private Refresh(String description) {
        super(description);
    }

    /**
     * Builds the refreshing in the first window.
     *
     * @return built the refreshing action
     */
    public static Refresh refresh() {
        return refresh(window());
    }

    /**
     * Builds the refreshing in some window which should be found.
     *
     * @param windowSupplier is how to get the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refresh(GetWindowSupplier windowSupplier) {
        return new Refresh(format(DESCRIPTION, windowSupplier)).performOn(windowSupplier);
    }

    /**
     * Builds the refreshing in the window.
     *
     * @param window is the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refresh(Window window) {
        return new Refresh(format(DESCRIPTION, window)).performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.refresh();
    }
}
