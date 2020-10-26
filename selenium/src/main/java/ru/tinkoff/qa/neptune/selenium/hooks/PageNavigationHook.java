package ru.tinkoff.qa.neptune.selenium.hooks;


import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;
import ru.tinkoff.qa.neptune.core.api.hooks.HookOrder;

import java.lang.reflect.Method;

/**
 * Manages the browser page navigating before method execution.
 * Priority is 10. This priority provides a lot of place for customer's hook to be executed
 */
@HookOrder(priority = 10)
public class PageNavigationHook implements ExecutionHook {

    @Override
    public void executeMethodHook(Method method, Object on, Class<?> classOfTarget) {

    }
}
