package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@CaptureOnFailure(by = WebDriverImageCaptor.class)
@CaptureOnSuccess(by = WebDriverImageCaptor.class)
@Description("Navigate back")
@SequentialActionSupplier.DefinePerformOnParameterName("Window/tab to perform navigation back")
@MaxDepthOfReporting(1)
@IncludeParamsOfInnerGetterStep
public final class Back extends SequentialActionSupplier<SeleniumStepContext, Window, Back> {

    private Back() {
        super();
    }

    /**
     * Builds navigation back in the browser window/tab that active currently.
     *
     * @return built navigation action
     */
    public static Back back() {
        return back(currentWindow());
    }

    /**
     * Builds navigation back in some window which should be found.
     *
     * @param windowSupplier is how to get the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(GetWindowSupplier windowSupplier) {
        return new Back().performOn(windowSupplier);
    }

    /**
     * Builds navigation back in the window.
     *
     * @param window is the window where navigation should be performed
     * @return built navigation action
     */
    public static Back back(Window window) {
        return new Back().performOn(window);
    }

    @Override
    protected void howToPerform(Window value) {
        value.back();
    }
}
