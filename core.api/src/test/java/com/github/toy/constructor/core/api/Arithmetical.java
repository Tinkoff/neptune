package com.github.toy.constructor.core.api;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public class Arithmetical extends GetSupplier<CalculatorSteps, Number, Arithmetical> {

    public static Arithmetical number(Number number) {
        return new Arithmetical().set(toGet(format("Entering number %s", number.toString()),
                calculatorSteps -> calculatorSteps.setCalculated(number.doubleValue())));
    }

    public static Arithmetical append(Number toAppend) {
        return new Arithmetical().set(toGet(format("Appending number %s", toAppend),
                calculatorSteps -> calculatorSteps.setCalculated(calculatorSteps.get() + toAppend.doubleValue())));
    }

    public static Arithmetical subtract(Number toSubtract) {
        return new Arithmetical().set(toGet(format("Subtract number %s", toSubtract),
                calculatorSteps -> calculatorSteps.setCalculated(calculatorSteps.get() - toSubtract.doubleValue())));
    }

    public static Arithmetical multiply(Number toMultiply) {
        return new Arithmetical().set(toGet(format("Multiplying by number %s", toMultiply),
                calculatorSteps -> calculatorSteps.setCalculated(calculatorSteps.get() * toMultiply.doubleValue())));
    }

    public static Arithmetical divide(Number toDivide) {
        checkArgument(toDivide.doubleValue() != 0, "Number to divide by should differ from zero");
        return new Arithmetical().set(toGet(format("Divide by number %s", toDivide),calculatorSteps ->
                calculatorSteps.setCalculated(calculatorSteps.get() / toDivide.doubleValue())));
    }
}
