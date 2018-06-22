package com.github.toy.constructor.testng.integration.test;

import com.github.toy.constructor.testng.integration.BaseTestNgTest;

public class BaseTestNgIntegrationTest extends BaseTestNgTest {

    private StepClass1 stepClass1;
    private StepClass2 stepClass2;
    private final StepClass3 stepClass3 = null;
    private StepClass4 stepClass4;

    public StepClass1 getStepClass1() {
        return stepClass1;
    }

    public StepClass2 getStepClass2() {
        return stepClass2;
    }

    public StepClass3 getStepClass3() {
        return stepClass3;
    }

    public StepClass4 getStepClass4() {
        return stepClass4;
    }
}
