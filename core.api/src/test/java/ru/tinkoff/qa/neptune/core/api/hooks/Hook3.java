package ru.tinkoff.qa.neptune.core.api.hooks;

import java.lang.reflect.Method;

@HookOrder(priority = 1)
public class Hook3 implements ExecutionHook {

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {

    }
}
