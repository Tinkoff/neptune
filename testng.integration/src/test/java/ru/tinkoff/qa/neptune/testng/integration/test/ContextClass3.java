package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.ProviderOfEmptyParameters;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class ContextClass3 implements GetStepContext<ContextClass3> {
}
