package com.github.toy.constructor.core.api;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;

abstract class ArithmeticalSequence extends SequentialGetSupplier<CalculatorSteps, Number, Number, ArithmeticalSequence> {

    private static ArithmeticalSequence getResulted(ArithmeticalSequence sequence,
                                                    GetSupplier<CalculatorSteps, Number, ?> supplier) {
        return sequence.from(supplier);
    }

    static ArithmeticalSequence appendToResultOf(Number toAppend, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("Appending number %s", toAppend),
                        number -> number.doubleValue() + toAppend.doubleValue());
            }
        }, result);
    }

    static ArithmeticalSequence subtractFromResultOf(Number toSubtract, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("Subtract number %s", toSubtract), number -> number.doubleValue() - toSubtract.doubleValue());
            }
        }, result);
    }

    static ArithmeticalSequence multiplyByResultOf(Number toMultiply, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("Multiplying by number %s", toMultiply), number -> number.doubleValue() * toMultiply.doubleValue());
            }
        }, result);
    }

    static ArithmeticalSequence divideByResultOf(Number toDivide, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("Divide by number %s", toDivide), number -> number.doubleValue() / toDivide.doubleValue());
            }
        }, result);
    }
}