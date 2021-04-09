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
@Description("Refresh window/tab")
@SequentialActionSupplier.DefinePerformOnParameterName("Window/tab to refresh")
@MaxDepthOfReporting(1)
@IncludeParamsOfInnerGetterStep
public final class Refresh extends SequentialActionSupplier<SeleniumStepContext, Window, Refresh> {

    private Refresh() {
        super();
    }

    /**
     * Builds the refreshing in the browser window/tab that active currently.
     *
     * @return built the refreshing action
     */
    public static Refresh refreshWindow() {
        return refreshWindow(currentWindow());
    }

    /**
     * Builds the refreshing in some window which should be found.
     *
     * @param windowSupplier is how to get the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refreshWindow(GetWindowSupplier windowSupplier) {
        return new Refresh().performOn(windowSupplier);
    }

    /**
     * Builds the refreshing in the window.
     *
     * @param window is the window where the refreshing should be performed
     * @return built the refreshing action
     */
    public static Refresh refreshWindow(Window window) {
        return new Refresh().performOn(window);
    }

    @Override
    protected void performActionOn(Window value) {
        value.refresh();
    }
}
