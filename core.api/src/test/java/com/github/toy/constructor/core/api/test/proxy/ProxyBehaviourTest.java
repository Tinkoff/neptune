package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.GetSupplier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static com.github.toy.constructor.core.api.test.proxy.Arithmetical.*;
import static com.github.toy.constructor.core.api.test.proxy.ArithmeticalSequence.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProxyBehaviourTest {

    private CalculatorSteps calculator;

    @BeforeClass
    public void beforeAll() throws Exception {
        calculator = getSubstituted(CalculatorSteps.class, params(), new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }

            @Override
            public String value() {
                return "Test value";
            }
        });
    }

    @DataProvider(parallel = true)
    public Object[][] getData() {
        return new Object[][]{
                {appendToResultOf(7, subtractFromResultOf(7, number(6))), 6},
                {multiplyByResultOf(9, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, number(9))))), 47.25},
                {subtractFromResultOf(10, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, number(9))))), -4.75},
                {subtractFromResultOf(10, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, multiply(8))))), -10},
                {appendToResultOf(8, append(4)), 12},
                {appendToResultOf(6, subtract(12)), -6},
                {appendToResultOf(6, divide(12)), 6},

                {appendToResultOf(7, subtractFromResultOf(7, number(6))), 6},
                {multiplyByResultOf(9, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, number(9))))), 47.25},
                {subtractFromResultOf(10, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, number(9))))), -4.75},
                {subtractFromResultOf(10, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, multiply(8))))), -10},
                {appendToResultOf(8, append(4)), 12},
                {appendToResultOf(6, subtract(12)), -6},
                {appendToResultOf(6, divide(12)), 6},

                {appendToResultOf(7, subtractFromResultOf(7, number(6))), 6},
                {multiplyByResultOf(9, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, number(9))))), 47.25},
                {subtractFromResultOf(10, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, number(9))))), -4.75},
                {subtractFromResultOf(10, divideByResultOf(6, multiplyByResultOf(7, divideByResultOf(2, multiply(8))))), -10},
                {appendToResultOf(8, append(4)), 12},
                {appendToResultOf(6, subtract(12)), -6},
                {appendToResultOf(6, divide(12)), 6}
        };
    }

    @BeforeMethod
    public void beforeTest() {
        calculator.reset();
    }

    @Test(threadPoolSize = 6, dataProvider = "getData")
    public void threadSafetyTest(GetSupplier<CalculatorSteps, Number, ?> calculation, Number number) {
        assertThat("Result of calculation", calculator.get(calculation), is(number.doubleValue()));
    }
}
