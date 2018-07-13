package com.github.toy.constructor.testng.integration.test;

import com.github.toy.constructor.core.api.*;
import com.github.toy.constructor.core.api.cleaning.Refreshable;
import com.github.toy.constructor.core.api.cleaning.Stoppable;

@CreateWith(provider = ABParameterProvider.class)
public class StepClass2 implements GetStep<StepClass2>, PerformActionStep<StepClass2>, Refreshable, Stoppable {

    private final int a;
    private final int b;
    private int refreshCount;
    private int stopCount;

    public StepClass2(int a, int b) {
        this.a = a;
        this.b = b;
    }

    int getA() {
        return a;
    }

    int getB() {
        return b;
    }

    @Override
    public void refresh() {
        refreshCount ++;
    }

    @Override
    public void shutDown() {
        stopCount ++;
    }

    int getRefreshCount() {
        return refreshCount;
    }

    int getStopCount() {
        return stopCount;
    }
}
