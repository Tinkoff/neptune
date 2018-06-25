package com.github.toy.constructor.testng.integration.test;

import com.github.toy.constructor.core.api.CreateWith;
import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.ProviderOfEmptyParameters;
import com.github.toy.constructor.core.api.Refreshable;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class StepClass1 implements PerformStep<StepClass1>, Refreshable {

    private int refreshCount;

    @Override
    public void refresh() {
        refreshCount++;
    }


    public int getRefreshCount() {
        return refreshCount;
    }
}
