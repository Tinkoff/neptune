package ru.tinkoff.qa.neptune.jupiter.integration;

import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;

@CreateWith(provider = ABParameterProvider.class)
public class ContextClass2 extends Context implements ContextRefreshable {

    static final ContextClass2 context = getInstance(ContextClass2.class);
    private static int refreshCount;
    private final int a;
    private final int b;

    public ContextClass2(int a, int b) {
        this.a = a;
        this.b = b;
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
