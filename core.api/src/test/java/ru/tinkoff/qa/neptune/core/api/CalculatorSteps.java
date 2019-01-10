package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.steps.performer.ActionStepPerformer;
import ru.tinkoff.qa.neptune.core.api.steps.performer.GetStepPerformer;

import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class CalculatorSteps implements ActionStepPerformer<CalculatorSteps>, GetStepPerformer<CalculatorSteps>, Supplier<Double> {
    private Double calculated = 0D;

    void reset() {
        perform(StoryWriter.action("Reset calculated value to 0",
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
