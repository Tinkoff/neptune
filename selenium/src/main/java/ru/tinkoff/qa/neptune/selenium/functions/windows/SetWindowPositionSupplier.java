package ru.tinkoff.qa.neptune.selenium.functions.windows;

import org.openqa.selenium.Point;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Window/tab to change position of"
)
public final class SetWindowPositionSupplier extends SequentialActionSupplier<SeleniumStepContext, Window, SetWindowPositionSupplier> {

    @StepParameter("New position")
    private final Point position;

    private SetWindowPositionSupplier(Point position) {
        super("Change position of the browser window/tab");
        checkArgument(nonNull(position), "A new position should not be a null value");
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
        return new SetWindowPositionSupplier(position).performOn(supplier);
    }

    /**
     * Builds an action which changes position of the window.
     *
     * @param window   to change position of
     * @param position is the new position of the window
     * @return Supplier of an action which changes window position.
     */
    public static SetWindowPositionSupplier setPositionOf(Window window, Point position) {
        return new SetWindowPositionSupplier(position).performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.setPosition(position);
    }
}
