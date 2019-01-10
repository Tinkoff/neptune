package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ActionStepPerformer;
import ru.tinkoff.qa.neptune.core.api.steps.performer.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.performer.GetStepPerformer;

@CreateWith(provider = ABParameterProvider.class)
public class StepClass2 implements GetStepPerformer<StepClass2>, ActionStepPerformer<StepClass2>, Refreshable {

    private final int a;
    private final int b;
    private static int refreshCount;

    public StepClass2(int a, int b) {
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
    public void refresh() {
        refreshCount ++;
    }

    static int getRefreshCount() {
        return refreshCount;
    }

    static void refreshCountToZero() {
        refreshCount = 0;
    }
}
