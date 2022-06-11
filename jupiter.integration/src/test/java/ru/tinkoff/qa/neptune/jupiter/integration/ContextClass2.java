package ru.tinkoff.qa.neptune.jupiter.integration;

import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

public class ContextClass2 extends Context<ContextClass2> implements ContextRefreshable {

    private static int refreshCount;
    private final int a;
    private final int b;

    public ContextClass2() {
        this.a = 1;
        this.b = 2;
    }

    static int getRefreshCount() {
        return refreshCount;
    }

    static void refreshCountToZero() {
        refreshCount = 0;
    }

    int getA() {
        return a;
    }

    int getB() {
        return b;
    }

    @Override
    public void refreshContext() {
        refreshCount++;
    }
}
