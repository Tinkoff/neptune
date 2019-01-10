package ru.tinkoff.qa.neptune.core.api.steps.performer;

import ru.tinkoff.qa.neptune.core.api.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.performer.ActionStepPerformer;
import ru.tinkoff.qa.neptune.core.api.steps.performer.GetStepPerformer;

public interface ParameterProvider {
    /**
     * Creates an instance which wraps parameters for constructor invocation of some subclass of {@link ActionStepPerformer}
     * or {@link GetStepPerformer}.
     *
     * @return instance of {@link ConstructorParameters}.
     */
    ConstructorParameters provide();
}
