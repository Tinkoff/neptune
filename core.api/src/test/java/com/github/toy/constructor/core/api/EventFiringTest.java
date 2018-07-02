package com.github.toy.constructor.core.api;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.Arithmetical.divide;
import static com.github.toy.constructor.core.api.ArithmeticalSequence.divideByResultOf;
import static com.github.toy.constructor.core.api.ArithmeticalSequence.multiplyByResultOf;
import static com.github.toy.constructor.core.api.ArithmeticalSequence.subtractFromResultOf;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.TestEventLogger.MESSAGES;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static com.github.toy.constructor.core.api.Arithmetical.number;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.FAILURE;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.SUCCESS;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.SUCCESS_AND_FAILURE;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;

public class EventFiringTest {

    private CalculatorSteps calculator;

    private final GetSupplier<CalculatorSteps, Number, ?> calculation =
            subtractFromResultOf(100,
                            divideByResultOf(0.5,
                                    multiplyByResultOf(11,
                                            divideByResultOf(-6,
                                                    number(9)))));

    @BeforeClass
    public void beforeAll() {
        calculator = getSubstituted(CalculatorSteps.class, params());
    }

    private void prepare() {
        TestCaptor.messages.clear();
        TestCapturedStringInjector.messages.clear();
        TestCapturedFileInjector.messages.clear();
        MESSAGES.clear();

        calculator.reset();
        calculator.get(toGet("Calculation",
                calculator1 -> calculator1.get(calculation).floatValue() + 5F));
        try {
            calculator.get(divide(0F));
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void captorTestWhenTypeOfEventIsNotDefined() {
        prepare();
        assertThat("Check messages logged by SPI logger",
                TestCaptor.messages,
                emptyIterable());

        assertThat("Check messages logged by SPI String logger",
                TestCapturedStringInjector.messages,
                emptyIterable());

        assertThat("Check messages logged by SPI File logger",
                TestCapturedFileInjector.messages,
                emptyIterable());
    }

    @Test
    public void captorTestWhenTypeOfEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    contains("Getting of 'Entering number 9' succeed. Result: 9.0",
                            "Getting of 'Divide by number -6' succeed. Result: -1.5",
                            "Getting of 'Multiplying by number 11' succeed. Result: -16.5",
                            "Getting of 'Divide by number 0.5' succeed. Result: -33.0",
                            "Getting of 'Subtract number 100' succeed. Result: -133.0",
                            "Getting of 'Calculation' succeed. Result: -128.0"));

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains("Performing of 'Reset calculated value to 0' succeed. Result: Calculator",
                            "Getting of 'Entering number 9' succeed. Result: 9.0",
                            "Getting of 'Divide by number -6' succeed. Result: -1.5",
                            "Getting of 'Multiplying by number 11' succeed. Result: -16.5",
                            "Getting of 'Divide by number 0.5' succeed. Result: -33.0",
                            "Getting of 'Subtract number 100' succeed. Result: -133.0",
                            "Getting of 'Calculation' succeed. Result: -128.0"));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void captorTestWhenTypeOfEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    emptyIterable());

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains("Getting of 'Divide by number 0.0' failed. Result: Calculator"));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void captorTestWhenTypeOfEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    contains("Getting of 'Entering number 9' succeed. Result: 9.0",
                            "Getting of 'Divide by number -6' succeed. Result: -1.5",
                            "Getting of 'Multiplying by number 11' succeed. Result: -16.5",
                            "Getting of 'Divide by number 0.5' succeed. Result: -33.0",
                            "Getting of 'Subtract number 100' succeed. Result: -133.0",
                            "Getting of 'Calculation' succeed. Result: -128.0"));

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains("Performing of 'Reset calculated value to 0' succeed. Result: Calculator",
                            "Getting of 'Entering number 9' succeed. Result: 9.0",
                            "Getting of 'Divide by number -6' succeed. Result: -1.5",
                            "Getting of 'Multiplying by number 11' succeed. Result: -16.5",
                            "Getting of 'Divide by number 0.5' succeed. Result: -33.0",
                            "Getting of 'Subtract number 100' succeed. Result: -133.0",
                            "Getting of 'Calculation' succeed. Result: -128.0",
                            "Getting of 'Divide by number 0.0' failed. Result: Calculator"));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void eventFiringTest() {
        prepare();
        assertThat(MESSAGES, contains("Perform Reset calculated value to 0 has started",
                "Event finished",
                "Get Calculation has started",
                "Get Entering number 9 has started",
                "9.0 has been returned",
                "Event finished",
                "From 9.0 get Divide by number -6 has started",
                "-1.5 has been returned",
                "Event finished",
                "From -1.5 get Multiplying by number 11 has started",
                "-16.5 has been returned",
                "Event finished",
                "From -16.5 get Divide by number 0.5 has started",
                "-33.0 has been returned",
                "Event finished",
                "From -33.0 get Subtract number 100 has started",
                "-133.0 has been returned",
                "Event finished",
                "-128.0 has been returned",
                "Event finished",
                "Get Divide by number 0.0 has started",
                "java.lang.ArithmeticException has been thrown",
                "Event finished"));
    }
}
