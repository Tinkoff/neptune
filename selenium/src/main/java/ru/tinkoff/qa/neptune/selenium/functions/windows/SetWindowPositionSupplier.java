package ru.tinkoff.qa.neptune.selenium.functions.windows;

import org.openqa.selenium.Point;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
@SequentialActionSupplier.DefinePerformOnParameterName("Window/tab to change position of")
@Description("Set new position {newPosition} of the browser window/tab on screen")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class SetWindowPositionSupplier extends SequentialActionSupplier<SeleniumStepContext, Window, SetWindowPositionSupplier> {

    @DescriptionFragment("newPosition")
    private final Point position;

    private SetWindowPositionSupplier(Point position) {
        super();
        checkArgument(nonNull(position), "A new position should not be a null value");
        this.position = position;
    }

    /**
     * Builds an action which changes position of the active window.
     *
     * @param position is the new position of the window
     * @return Supplier of an action which changes position of the active window.
     */
    public static SetWindowPositionSupplier setWindowPosition(Point position) {
        return setPositionOf(currentWindow(), position);
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
    protected void howToPerform(Window value) {
        value.setPosition(position);
    }
}
