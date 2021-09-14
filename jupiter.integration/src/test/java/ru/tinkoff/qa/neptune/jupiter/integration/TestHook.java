package ru.tinkoff.qa.neptune.jupiter.integration;

import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;

import java.lang.reflect.Method;

public class TestHook implements ExecutionHook {

    public static int count = 0;

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {
        count = count + 1;
    }
}
