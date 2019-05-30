package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

@CreateWith(provider = ABParameterProvider.class)
public class ContextClass2 implements GetStepContext<ContextClass2>, ActionStepContext<ContextClass2>, ContextRefreshable {

    private final int a;
    private final int b;
    private static int refreshCount;

    public ContextClass2(int a, int b) {
        this.a = a;
        this.b = b;
    }

    int getA() {
        return a;
    }

    int getB() {
        return b;
    }

    @Override
    public void refreshContext() {
        refreshCount ++;
    }

    static int getRefreshCount() {
        return refreshCount;
    }

    static void refreshCountToZero() {
        refreshCount = 0;
    }
}
