package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.steps.performer.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.performer.GetStepPerformer;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ProviderOfEmptyParameters;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class StepClass3 implements GetStepPerformer<StepClass3> {
}
