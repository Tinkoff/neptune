package com.github.toy.constructor.selenium.functions.target.locator.window;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.Point;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;

public final class SetWindowPositionSupplier extends SequentialActionSupplier<SeleniumSteps, Window, SetWindowPositionSupplier> {

    private SetWindowPositionSupplier() {
        super();
    }

    /**
     * Builds an action which changes position of the first window.
     *
     * @param position is the new position of the window
     * @return Supplier of an action which changes position of the first window.
     */
    public static SetWindowPositionSupplier setWindowPosition(Point position) {
        return setPositionOf(window(), position);
    }

    /**
     * Builds an action which changes position of some window.
     *
     * @param supplier is how to get the window to change position
     * @param position is the new position of the window
     * @return Supplier of an action which changes window position.
     */
    public static SetWindowPositionSupplier setPositionOf(GetWindowSupplier supplier, Point position) {
        return new SetWindowPositionSupplier().andSetPositionOf(supplier, position);
    }

    /**
     * Builds an action which changes position of the window.
     *
     * @param window to change position of
     * @param position is the new position of the window
     * @return Supplier of an action which changes window position.
     */
    public static SetWindowPositionSupplier setPositionOf(Window window, Point position) {
        return new SetWindowPositionSupplier().andSetPositionOf(window, position);
    }

    /**
     * Adds another action which changes position of some window.
     *
     * @param supplier is how to get the window to change position
     * @param position is the new position of the window
     * @return self-reference.
     */
    public SetWindowPositionSupplier andSetPositionOf(GetWindowSupplier supplier, Point position) {
        return andThen("Set position of the window", supplier, position);
    }

    /**
     * Adds another action which changes position of the window.
     *
     * @param window to change position of
     * @param position is the new position of the window
     * @return self-reference.
     */
    public SetWindowPositionSupplier andSetPositionOf(Window window, Point position) {
        return andThen("Set position of the window", window, position);
    }

    @Override
    protected void performActionOn(Window value, Object... additionalArgument) {
        value.setPosition(Point.class.cast(additionalArgument[0]));
    }
}
