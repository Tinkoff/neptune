package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.StepMark;

import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class CalculatorSteps implements PerformStep<CalculatorSteps>, GetStep<CalculatorSteps>, Supplier<Double> {
    private Double calculated = 0D;

    @StepMark(constantMessagePart = "Reset calculated value to 0")
    public void reset() {
        calculated = 0D;
    }

    @Override
    public Double get() {
        return privateGet();
    }

    //This method was added for additional test coverage
    private Double privateGet() {
        return calculated;
    }

    Double setCalculated(Double number) {
        calculated = ofNullable(number).orElse(calculated);
        return calculated;
    }
}
