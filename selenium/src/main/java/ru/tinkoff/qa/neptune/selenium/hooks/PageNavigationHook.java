package ru.tinkoff.qa.neptune.selenium.hooks;


import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;
import ru.tinkoff.qa.neptune.core.api.hooks.HookOrder;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.hooks.DefaultBrowserPage.DefaultBrowserPageReader.getDefaultBrowserPageToNavigate;
import static ru.tinkoff.qa.neptune.selenium.hooks.ForceNavigation.ForceNavigationReader.getBrowserPageToNavigate;

/**
 * Manages the browser page navigating before method execution.
 * Priority is 10. This priority provides a lot of place for customer's hook to be executed
 */
@HookOrder(priority = 10)
public final class PageNavigationHook implements ExecutionHook {

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {
        var toNavigateTo = ofNullable(getDefaultBrowserPageToNavigate(on, method, isTest))
                .orElseGet(() -> getBrowserPageToNavigate(on, method));

        if (toNavigateTo != null) {
            inBrowser().navigateTo(toNavigateTo);
        }
    }
}
