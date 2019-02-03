package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.action;

public class CalculatorSteps implements ActionStepContext<CalculatorSteps>, GetStepContext<CalculatorSteps>, Supplier<Double> {
    private Double calculated = 0D;

    void reset() {
        perform(action("Reset calculated value to 0",
                calculatorSteps -> calculatorSteps.calculated = 0D));
    }

    @Override
    public Double get() {
        return calculated;
    }


    Double setCalculated(Double number) {
        calculated = ofNullable(number).orElse(calculated);
        return calculated;
    }
}
