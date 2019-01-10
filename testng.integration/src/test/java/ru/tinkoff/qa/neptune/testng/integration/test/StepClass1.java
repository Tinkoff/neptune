package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.steps.performer.ActionStepPerformer;
import ru.tinkoff.qa.neptune.core.api.steps.performer.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ProviderOfEmptyParameters;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class StepClass1 implements ActionStepPerformer<StepClass1>, Refreshable {

    private static int refreshCount;

    @Override
    public void refresh() {
        refreshCount++;
    }


    public static int getRefreshCount() {
        return refreshCount;
    }
}
