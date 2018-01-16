package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.core.api.SequentialGetSupplier;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

abstract class ArithmeticalSequence extends SequentialGetSupplier<CalculatorSteps, Number, Number, ArithmeticalSequence> {

    private static ArithmeticalSequence getResulted(ArithmeticalSequence sequence,
                                                    GetSupplier<CalculatorSteps, Number, ?> supplier) {
        return sequence.from(supplier);
    }

    public static ArithmeticalSequence appendToResultOf(Number toAppend, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("+%s", toAppend), EMPTY, EMPTY,
                        number -> number.doubleValue() + toAppend.doubleValue());
            }
        }, result);
    }

    public static ArithmeticalSequence subtractFromResultOf(Number toSubtract, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("-%s", toSubtract), EMPTY, EMPTY,
                        number -> number.doubleValue() - toSubtract.doubleValue());
            }
        }, result);
    }

    public static ArithmeticalSequence multiplyByResultOf(Number toMultiply, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("*%s", toMultiply), EMPTY, EMPTY, number -> number.doubleValue() * toMultiply.doubleValue());
            }
        }, result);
    }

    public static ArithmeticalSequence divideByResultOf(Number toDivide, GetSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return toGet(format("/%s", toDivide), EMPTY, EMPTY, number -> number.doubleValue() / toDivide.doubleValue());
            }
        }, result);
    }

    @Override
    protected ArithmeticalSequence from(GetSupplier<CalculatorSteps, Number, ?> supplier) {
        super.from(supplier);
        Function<CalculatorSteps, Number> wrapped = get();
        set(toGet(wrapped.toString(), wrapped));
        return this;
    }
}