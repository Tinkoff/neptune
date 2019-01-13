package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class Forward extends NavigationActionSupplier<Forward> {

    private static final String DESCRIPTION = "Navigate forward in %s";

    private Forward(String description) {
        super(description);
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
        return new Forward(format(DESCRIPTION, windowSupplier)).performOn(windowSupplier);
    }

    /**
     * Builds navigation forward in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Forward forward(Window window) {
        return new Forward(format(DESCRIPTION, window)).performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.forward();
    }
}
