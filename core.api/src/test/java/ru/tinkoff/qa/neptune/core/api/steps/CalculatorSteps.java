package ru.tinkoff.qa.neptune.core.api.steps;


import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class CalculatorSteps extends Context<CalculatorSteps> implements Supplier<Double> {

    private static final CalculatorSteps calculator = getInstance(CalculatorSteps.class);
    private Double calculated = 0D;

    public static CalculatorSteps calculator() {
        return calculator;
    }

    @Override
    public Double get() {
        return calculated;
    }

    public void set(Double value) {
        this.calculated = value;
    }

    public Number evaluate(SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> toEvaluate) {
        return get(toEvaluate);
    }

    public Number evaluate(Function<CalculatorSteps, Number> toEvaluate) {
        return toEvaluate.apply(this);
    }


    Double setCalculated(Double number) {
        calculated = ofNullable(number).orElse(calculated);
        return calculated;
    }
}
