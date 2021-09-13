package ru.tinkoff.qa.neptune.jupiter.integration;

import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

public class ContextClass1 extends Context<ContextClass1> implements ContextRefreshable {

    static final ContextClass1 context = getInstance(ContextClass1.class);
    private static int refreshCount;

    public static int getRefreshCount() {
        return refreshCount;
    }

    @Override
    public void refreshContext() {
        refreshCount++;
    }
}
