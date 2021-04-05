package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

@Description("URL of the loaded page")
@SequentialGetStepSupplier.DefineFromParameterName("Window/tab to get URL from")
public final class GetCurrentUrlSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, String, Window, GetCurrentUrlSupplier> {

    private GetCurrentUrlSupplier() {
        super(Window::getCurrentUrl);
    }

    /**
     * Builds a function which returns url of the page loaded in the browser window/tab that active currently.
     *
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the current window/tab.
     */
    public static GetCurrentUrlSupplier currentUrl() {
        return new GetCurrentUrlSupplier().from(currentWindow());
    }

    /**
     * Builds a function which returns url of the page loaded in some window/tab. This window/tab is supposed to be found.
     *
     * @param from is how to find the window where loaded url should be taken
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in some window/tab which should be got by criteria.
     */
    public static GetCurrentUrlSupplier currentUrl(GetWindowSupplier from) {
        return new GetCurrentUrlSupplier().from(from);
    }

    /**
     * Builds a function which returns url of the page loaded in the window/tab.
     *
     * @param from is the window where loaded url should be taken
     * @return n instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the window/tab.
     */
    public static GetCurrentUrlSupplier currentUrl(Window from) {
        return new GetCurrentUrlSupplier().from(from);
    }
}
