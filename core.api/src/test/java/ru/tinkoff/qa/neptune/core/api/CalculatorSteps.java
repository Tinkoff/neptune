package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.steps.StoryWriter;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class CalculatorSteps implements ActionStepContext<CalculatorSteps>, GetStepContext<CalculatorSteps>, Supplier<Double> {
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
