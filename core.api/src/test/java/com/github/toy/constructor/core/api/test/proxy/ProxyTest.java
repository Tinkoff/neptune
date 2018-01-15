package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class ProxyTest {

    private CalculatorSteps calculator;

    @BeforeClass
    public void beforeAll() throws Exception {
        calculator = getSubstituted(CalculatorSteps.class, params());
    }

    @Test
    public void tezt() {

    }

    private static Double rememberValue(CalculatorSteps calculatorSteps, Double resulted) {
        calculatorSteps.setCalculated(resulted);
        return calculatorSteps.get();
    }


    static class CalculatorSteps implements GetStep<CalculatorSteps>, PerformStep<CalculatorSteps>, Supplier<Double> {
        private Double calculated = 0D;

        @ToBeReported(constantMessagePart = "Reset calculated value to 0")
        public void reset() {
            calculated = 0D;
        }

        @Override
        public Double get() {
            return calculated;
        }

        void setCalculated(Double number) {
            calculated = ofNullable(number).orElse(calculated);
        }
    }

    static class Arithmetical extends GetSupplier<CalculatorSteps, Double, Arithmetical> {

        public static Arithmetical number(Number number) {
            return new Arithmetical().set(format("Number %s", number),
                    calculatorSteps ->
                            rememberValue(calculatorSteps, number.doubleValue()));
        }

        public static Arithmetical append(Number toAppend) {
            return new Arithmetical().set(format("Add number %s", toAppend),
                    calculatorSteps ->
                            rememberValue(calculatorSteps,
                                    calculatorSteps.get() + toAppend.doubleValue()));
        }

        public static Arithmetical substract(Number toSubtract) {
            return new Arithmetical().set(format("Subtract number %s", toSubtract),
                    calculatorSteps ->
                            rememberValue(calculatorSteps,
                                    calculatorSteps.get() - toSubtract.doubleValue()));
        }

        public static Arithmetical multiply(Number toMultiply) {
            return new Arithmetical().set(format("Multiply by %s", toMultiply),
                    calculatorSteps -> rememberValue(calculatorSteps,
                            calculatorSteps.get() * toMultiply.doubleValue()));
        }

        public static Arithmetical divide(Number toDivide) {
            checkArgument(toDivide.doubleValue() != 0, "Number to divide by should differ from zero");
            return new Arithmetical().set(format("Divide by %s", toDivide),
                    calculatorSteps -> rememberValue(calculatorSteps,
                            calculatorSteps.get() / toDivide.doubleValue()));
        }
    }

    static abstract class ArithmeticalSequence extends SequentialGetSupplier<CalculatorSteps, Double, Number, ArithmeticalSequence> {
        @Override
        protected ArithmeticalSequence from(GetSupplier<CalculatorSteps, Number, ?> supplier) {
            Function<CalculatorSteps, Double> wrapped = get();
            super.from(supplier);
            set(wrapped.toString(), calculatorSteps -> rememberValue(calculatorSteps, wrapped.apply(calculatorSteps)));
            return this;
        }

        public static ArithmeticalSequence appendToResultOf(Number toAppend, GetSupplier<CalculatorSteps, Double, ?> result) {
            return new ArithmeticalSequence() {
                @Override
                protected Function<Number, Double> getEndFunction() {
                    return toGet(format("Add number %s", toAppend), number -> number.doubleValue() + toAppend.doubleValue());
                }
            };
        }

        public static ArithmeticalSequence subtractFromResultOf(Number toSubtract, GetSupplier<CalculatorSteps, Double, ?> result) {
            return new ArithmeticalSequence() {
                @Override
                protected Function<Number, Double> getEndFunction() {
                    return toGet(format("Subtract number %s", toSubtract), number -> number.doubleValue() - toSubtract.doubleValue());
                }
            };
        }

        public static ArithmeticalSequence multiplyByResultOf(Number toMultiply, GetSupplier<CalculatorSteps, Double, ?> result) {
            return new ArithmeticalSequence() {
                @Override
                protected Function<Number, Double> getEndFunction() {
                    return toGet(format("Multiply by %s", toMultiply), number -> number.doubleValue() * toMultiply.doubleValue());
                }
            };
        }

        public static ArithmeticalSequence divideByResultOf(Number toDivide, GetSupplier<CalculatorSteps, Double, ?> result) {
            return new ArithmeticalSequence() {
                @Override
                protected Function<Number, Double> getEndFunction() {
                    return toGet(format("Divide by %s", toDivide), number -> number.doubleValue() / toDivide.doubleValue());
                }
            };
        }
    }
}
