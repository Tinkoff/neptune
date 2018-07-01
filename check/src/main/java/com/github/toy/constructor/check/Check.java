package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.CreateWith;
import com.github.toy.constructor.core.api.ProviderOfEmptyParameters;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class Check implements PerformStep<Check> {

    /**
     * This method performs the checking of some value by criteria.
     * @param that is a how to check some value.
     * @param <T> the type of the value to check
     * @return self-reference
     */
    public <T> Check verify(Value<T> that) {
        checkArgument(that != null, "Verifying should be described");
        return perform(that);
    }
}
