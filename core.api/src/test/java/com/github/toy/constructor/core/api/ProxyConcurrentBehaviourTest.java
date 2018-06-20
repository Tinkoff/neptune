package com.github.toy.constructor.core.api;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.Arithmetical.*;
import static com.github.toy.constructor.core.api.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.Substitution.getSubstituted;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProxyConcurrentBehaviourTest {

    private CalculatorSteps calculator;

    @DataProvider(parallel = true)
    public Object[][] getData() {
        return new Object[][]{
                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, divide(12)), 111},
        };
    }

    @BeforeMethod
    public synchronized void beforeTest() {
        calculator = ofNullable(calculator).orElseGet(() -> {
            try {
                return getSubstituted(CalculatorSteps.class, params());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        calculator.reset();
    }

    @Test(threadPoolSize = 4, dataProvider = "getData")
    public void threadSafetyTest(GetSupplier<CalculatorSteps, Number, ?> calculation, Number number) {
        assertThat("Result of calculation", calculator.get(calculation), is(number.doubleValue()));
    }
}
