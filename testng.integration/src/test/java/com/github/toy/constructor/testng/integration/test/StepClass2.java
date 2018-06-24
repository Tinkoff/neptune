package com.github.toy.constructor.testng.integration.test;

import com.github.toy.constructor.core.api.*;

@CreateWith(provider = ABParameterProvider.class)
public class StepClass2 implements GetStep<StepClass2>, PerformStep<StepClass2>, Refreshable, Stoppable {

    public static StepClass2 stepClass2;

    private final int a;
    private final int b;
    private int refreshCount;
    private int stopCount;

    public StepClass2(int a, int b) {
        this.a = a;
        this.b = b;
        stepClass2 = this;
    }

    public int getA() {
        return a;
    }

    public int getB() {
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

    public int getRefreshCount() {
        return refreshCount;
    }

    public int getStopCount() {
        return stopCount;
    }
}
