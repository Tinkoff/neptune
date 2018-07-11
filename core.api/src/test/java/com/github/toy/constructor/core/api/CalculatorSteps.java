package com.github.toy.constructor.core.api;

import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static java.util.Optional.ofNullable;

public class CalculatorSteps implements PerformActionStep<CalculatorSteps>, GetStep<CalculatorSteps>, Supplier<Double> {
    private Double calculated = 0D;

    void reset() {
        perform(action("Reset calculated value to 0",
                calculatorSteps -> calculatorSteps.calculated = 0D));
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

    public String toString() {
        return "Calculator";
    }
}
