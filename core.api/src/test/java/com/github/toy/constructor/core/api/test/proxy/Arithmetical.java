package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.GetSupplier;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

class Arithmetical extends GetSupplier<CalculatorSteps, Number, Arithmetical> {

    public static Arithmetical number(Number number) {
        return new Arithmetical().set(toGet(number.toString(), EMPTY, EMPTY,
                calculatorSteps -> calculatorSteps.setCalculated(number.doubleValue())));
    }

    public static Arithmetical append(Number toAppend) {
        return new Arithmetical().set(toGet(format("+%s", toAppend), EMPTY, EMPTY, calculatorSteps ->
                calculatorSteps.setCalculated(calculatorSteps.get() + toAppend.doubleValue())));
    }

    public static Arithmetical substract(Number toSubtract) {
        return new Arithmetical().set(toGet(format("-%s", toSubtract), EMPTY, EMPTY, calculatorSteps ->
                        calculatorSteps.setCalculated(calculatorSteps.get() - toSubtract.doubleValue())));
    }

    public static Arithmetical multiply(Number toMultiply) {
        return new Arithmetical().set(toGet(format("*%s", toMultiply), EMPTY, EMPTY, calculatorSteps ->
                        calculatorSteps.setCalculated(calculatorSteps.get() * toMultiply.doubleValue())));
    }

    public static Arithmetical divide(Number toDivide) {
        checkArgument(toDivide.doubleValue() != 0, "Number to divide by should differ from zero");
        return new Arithmetical().set(toGet(format("/%s", toDivide),EMPTY, EMPTY, calculatorSteps ->
                calculatorSteps.setCalculated(calculatorSteps.get() / toDivide.doubleValue())));
    }
}
