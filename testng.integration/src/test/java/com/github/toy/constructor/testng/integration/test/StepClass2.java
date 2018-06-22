package com.github.toy.constructor.testng.integration.test;

import com.github.toy.constructor.core.api.CreateWith;
import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;

@CreateWith(provider = ABParameterProvider.class)
public class StepClass2 implements GetStep<StepClass2>, PerformStep<StepClass2> {

    private final int a;
    private final int b;

    public StepClass2(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
