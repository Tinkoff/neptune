package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;
import ru.tinkoff.qa.neptune.core.api.ProviderOfEmptyParameters;
import ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class StepClass1 implements PerformActionStep<StepClass1>, Refreshable {

    private static int refreshCount;

    @Override
    public void refresh() {
        refreshCount++;
    }


    public static int getRefreshCount() {
        return refreshCount;
    }
}
