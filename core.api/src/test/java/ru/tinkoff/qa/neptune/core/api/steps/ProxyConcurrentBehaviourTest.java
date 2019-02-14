package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.ConstructorParameters;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;

public class ProxyConcurrentBehaviourTest {

    private CalculatorSteps calculator;

    @DataProvider(parallel = true)
    public Object[][] getData() {
        return new Object[][]{
                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, Arithmetical.number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, Arithmetical.number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, Arithmetical.multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, Arithmetical.divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, Arithmetical.number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, Arithmetical.number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, Arithmetical.multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, Arithmetical.divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, Arithmetical.number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, Arithmetical.number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, Arithmetical.multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, Arithmetical.divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, Arithmetical.number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, Arithmetical.number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, Arithmetical.multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, Arithmetical.divide(12)), 111},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(6))), 6},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), 47.25},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.number(9))))), -4.75},
                {ArithmeticalSequence.subtractFromResultOf(10, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(2, Arithmetical.multiply(8))))), -10},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.append(4)), 12},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.subtract(12)), -6},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.divide(12)), 6},

                {ArithmeticalSequence.appendToResultOf(7, ArithmeticalSequence.subtractFromResultOf(7, Arithmetical.number(-9))), -9},
                {ArithmeticalSequence.multiplyByResultOf(9, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(8, ArithmeticalSequence.divideByResultOf(3, Arithmetical.number(2.25))))), 9},
                {ArithmeticalSequence.subtractFromResultOf(100, ArithmeticalSequence.divideByResultOf(0.5, ArithmeticalSequence.multiplyByResultOf(11, ArithmeticalSequence.divideByResultOf(-6, Arithmetical.number(9))))), -133},
                {ArithmeticalSequence.subtractFromResultOf(10.5, ArithmeticalSequence.divideByResultOf(6, ArithmeticalSequence.multiplyByResultOf(7, ArithmeticalSequence.divideByResultOf(3, Arithmetical.multiply(111))))), -10.5},
                {ArithmeticalSequence.appendToResultOf(8, Arithmetical.subtract(4)), 4},
                {ArithmeticalSequence.appendToResultOf(6, Arithmetical.append(12)), 18},
                {ArithmeticalSequence.appendToResultOf(111, Arithmetical.divide(12)), 111},
        };
    }

    @BeforeMethod
    public synchronized void beforeTest() {
        calculator = ofNullable(calculator).orElseGet(() -> {
            try {
                return getProxied(CalculatorSteps.class, ConstructorParameters.params());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        calculator.reset();
    }

    @Test(threadPoolSize = 4, dataProvider = "getData")
    public void threadSafetyTest(SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> calculation, Number number) {
        assertThat("Result of calculation", calculator.get(calculation), is(number.doubleValue()));
    }
}
