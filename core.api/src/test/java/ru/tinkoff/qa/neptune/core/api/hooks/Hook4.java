package ru.tinkoff.qa.neptune.core.api.hooks;

import java.lang.reflect.Method;

@HookOrder(priority = 3)
public class Hook4 implements ExecutionHook {

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {

    }
}
