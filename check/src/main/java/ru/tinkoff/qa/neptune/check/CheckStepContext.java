package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.ProviderOfEmptyParameters;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class CheckStepContext implements ActionStepContext<CheckStepContext> {

    /**
     * This method performs the checking of some value by criteria.
     * @param that is a how to check some value.
     * @param <T> the type of the value to check
     * @return self-reference
     */
    public <T> CheckStepContext verify(ThatValue<T> that) {
        checkArgument(nonNull(that), "Verifying should be described");
        return perform(that);
    }
}
