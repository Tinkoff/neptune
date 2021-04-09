package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
@Description("Navigate forward")
@SequentialActionSupplier.DefinePerformOnParameterName("Window/tab to perform navigation forward")
@MaxDepthOfReporting(1)
@IncludeParamsOfInnerGetterStep
public final class Forward extends SequentialActionSupplier<SeleniumStepContext, Window, Forward> {

    private Forward() {
        super();
    }

    /**
     * Builds navigation forward in the browser window/tab that active currently.
     *
     * @return built navigation action
     */
    public static Forward forward() {
        return forward(currentWindow());
    }

    /**
     * Builds navigation forward in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static Forward forward(GetWindowSupplier windowSupplier) {
        return new Forward().performOn(windowSupplier);
    }

    /**
     * Builds navigation forward in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Forward forward(Window window) {
        return new Forward().performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.forward();
    }
}
