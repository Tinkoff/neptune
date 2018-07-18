package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.CreateWith;
import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.ProviderOfEmptyParameters;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class StepClass3 implements GetStep<StepClass3> {
}
