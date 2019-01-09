package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import org.openqa.selenium.Point;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

@MakeImageCapturesOnFinishing
public final class SetWindowPositionSupplier extends SequentialActionSupplier<SeleniumSteps, Window, SetWindowPositionSupplier> {

    private final Point position;

    private SetWindowPositionSupplier(Point position, String windowDescription) {
        super(format("Set position of the %s to %s", windowDescription, position));
        this.position = position;
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
        return new SetWindowPositionSupplier(position, supplier.toString()).performOn(supplier);
    }

    /**
     * Builds an action which changes position of the window.
     *
     * @param window to change position of
     * @param position is the new position of the window
     * @return Supplier of an action which changes window position.
     */
    public static SetWindowPositionSupplier setPositionOf(Window window, Point position) {
        return new SetWindowPositionSupplier(position, window.toString()).performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        checkArgument(nonNull(position), "A new position should not be a null value");
        value.setPosition(position);
    }
}
