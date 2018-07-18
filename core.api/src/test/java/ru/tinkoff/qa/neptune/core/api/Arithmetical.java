package ru.tinkoff.qa.neptune.core.api;

import java.math.BigDecimal;

import static java.lang.String.format;

class Arithmetical extends GetStepSupplier<CalculatorSteps, Number, Arithmetical> {

    static Arithmetical number(Number number) {
        return new Arithmetical().set(StoryWriter.toGet(format("Entering number %s", number.toString()),
                calculatorSteps -> calculatorSteps.setCalculated(number.doubleValue())));
    }

    static Arithmetical append(Number toAppend) {
        return new Arithmetical().set(StoryWriter.toGet(format("Appending number %s", toAppend),
                calculatorSteps -> calculatorSteps.setCalculated(calculatorSteps.get() + toAppend.doubleValue())));
    }

    static Arithmetical subtract(Number toSubtract) {
        return new Arithmetical().set(StoryWriter.toGet(format("Subtract number %s", toSubtract),
                calculatorSteps -> calculatorSteps.setCalculated(calculatorSteps.get() - toSubtract.doubleValue())));
    }

    static Arithmetical multiply(Number toMultiply) {
        return new Arithmetical().set(StoryWriter.toGet(format("Multiplying by number %s", toMultiply),
                calculatorSteps -> calculatorSteps.setCalculated(calculatorSteps.get() * toMultiply.doubleValue())));
    }

    static Arithmetical divide(Number toDivide) {
        return new Arithmetical().set(StoryWriter.toGet(format("Divide by number %s", toDivide), calculatorSteps ->
                calculatorSteps.setCalculated(new BigDecimal(calculatorSteps.get())
                        .divide(new BigDecimal(toDivide.doubleValue())).doubleValue())));
    }
}
