package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.GetSupplier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static com.github.toy.constructor.core.api.test.proxy.Arithmetical.number;
import static com.github.toy.constructor.core.api.test.proxy.ArithmeticalSequence.divideByResultOf;
import static com.github.toy.constructor.core.api.test.proxy.ArithmeticalSequence.multiplyByResultOf;
import static com.github.toy.constructor.core.api.test.proxy.ArithmeticalSequence.subtractFromResultOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ProxyLoggerTest {

    private CalculatorSteps calculator;
    private DefaultListLogger defaultListLogger = new DefaultListLogger();
    private SPIListLogger spiListLogger;

    private final GetSupplier<CalculatorSteps, Number, ?> calculation =
            subtractFromResultOf(100, divideByResultOf(0.5, multiplyByResultOf(11, divideByResultOf(-6, number(9)))));

    @BeforeClass
    public void beforeAll() throws Exception {
        calculator = getSubstituted(CalculatorSteps.class, params(), List.of(defaultListLogger));
        spiListLogger = SPIListLogger.getLogger();
    }

    @BeforeMethod
    public void beforeTest() {
        defaultListLogger.messages.clear();
        spiListLogger.messages.clear();
        calculator.reset();
        calculator.get(toGet("Get calculation",
                calculator1 -> calculator1.get(calculation).floatValue() + 5F));
    }

    @Test
    public void spiLoggerTest() {
        assertThat("Check messages logged by SPI logger",
                spiListLogger.messages,
                contains("SPI:Reset calculated value to 0",
                        "SPI:Get: Get calculation",
                        "SPI:Get: Entering number 9 ->\n" +
                                " Divide by number -6 ->\n" +
                                "  Multiplying by number 11 ->\n" +
                                "   Divide by number 0.5 ->\n" +
                                "    Subtract number 100",
                        "SPI:Returned value: -133.0",
                        "SPI:Returned value: -128.0"));
    }

    @Test
    public void customLoggerTest() {
        assertThat("Check messages logged by SPI logger",
                defaultListLogger.messages,
                contains("Reset calculated value to 0",
                        "Get: Get calculation",
                        "Get: Entering number 9 ->\n" +
                                " Divide by number -6 ->\n" +
                                "  Multiplying by number 11 ->\n" +
                                "   Divide by number 0.5 ->\n" +
                                "    Subtract number 100",
                        "Returned value: -133.0",
                        "Returned value: -128.0"));
    }
}
