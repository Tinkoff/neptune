package com.github.toy.constructor.core.api;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.ArithmeticalSequence.divideByResultOf;
import static com.github.toy.constructor.core.api.ArithmeticalSequence.multiplyByResultOf;
import static com.github.toy.constructor.core.api.ArithmeticalSequence.subtractFromResultOf;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.Substitution.getSubstituted;
import static com.github.toy.constructor.core.api.Arithmetical.number;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ProxyLoggerTest {

    private CalculatorSteps calculator;
    private SPIListLogger spiListLogger;

    private final GetSupplier<CalculatorSteps, Number, ?> calculation =
            subtractFromResultOf(100,
                            divideByResultOf(0.5,
                                    multiplyByResultOf(11,
                                            divideByResultOf(-6,
                                                    number(9)))));

    @BeforeClass
    public void beforeAll() throws Exception {
        calculator = getSubstituted(CalculatorSteps.class, params());
    }

    @BeforeMethod
    public void beforeTest() {
        SPIListLogger.messages.clear();
        calculator.reset();
        calculator.get(toGet("Get calculation",
                calculator1 -> calculator1.get(calculation).floatValue() + 5F));
    }

    @Test
    public void spiLoggerTest() {
        assertThat("Check messages logged by SPI logger",
                SPIListLogger.messages,
                contains("Performing of 'Reset calculated value to 0' succeed. Result: Calculator. Current value is 0.0",
                        "Getting of 'Entering number 9' succeed. Result: 9.0",
                        "Getting of 'Entering number 9' succeed. Result: 9.0",
                        "Getting of 'Divide by number -6' succeed. Result: -1.5",
                        "Getting of 'Multiplying by number 11' succeed. Result: -16.5",
                        "Getting of 'Divide by number 0.5' succeed. Result: -33.0",
                        "Getting of 'Subtract number 100' succeed. Result: -133.0",
                        "Getting of 'Get calculation' succeed. Result: -128.0"));
    }
}
