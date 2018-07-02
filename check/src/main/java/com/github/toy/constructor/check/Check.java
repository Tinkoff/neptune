package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.CreateWith;
import com.github.toy.constructor.core.api.ProviderOfEmptyParameters;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class Check implements PerformStep<Check> {

    /**
     * This method performs the checking of some thatValue by criteria.
     * @param that is a how to check some thatValue.
     * @param <T> the type of the thatValue to check
     * @return self-reference
     */
    public <T> Check verify(ThatValue<T> that) {
        checkArgument(that != null, "Verifying should be described");
        return perform(that);
    }
}
