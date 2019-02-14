package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.ProviderOfEmptyParameters;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class StepClass1 implements ActionStepContext<StepClass1>, ContextRefreshable {

    private static int refreshCount;

    @Override
    public void refreshContext() {
        refreshCount++;
    }


    public static int getRefreshCount() {
        return refreshCount;
    }
}
