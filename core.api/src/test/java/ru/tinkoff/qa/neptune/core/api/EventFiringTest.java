package ru.tinkoff.qa.neptune.core.api;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents;
import ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf;
import ru.tinkoff.qa.neptune.core.api.proxy.ProxyFactory;

import static ru.tinkoff.qa.neptune.core.api.ArithmeticalSequence.divideByResultOf;
import static ru.tinkoff.qa.neptune.core.api.ArithmeticalSequence.multiplyByResultOf;
import static ru.tinkoff.qa.neptune.core.api.ArithmeticalSequence.subtractFromResultOf;
import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;

public class EventFiringTest {

    private CalculatorSteps calculator;

    private final GetStepSupplier<CalculatorSteps, Number, ?> calculation =
            subtractFromResultOf(100,
                            divideByResultOf(0.5,
                                    multiplyByResultOf(11,
                                            divideByResultOf(-6,
                                                    Arithmetical.number(9)))));

    @BeforeClass
    public void beforeAll() {
        calculator = ProxyFactory.getProxied(CalculatorSteps.class, ConstructorParameters.params());
    }

    private void prepare() {
        TestCaptor.messages.clear();
        TestCapturedStringInjector.messages.clear();
        TestCapturedFileInjector.messages.clear();
        TestEventLogger.MESSAGES.clear();

        calculator.reset();
        calculator.get(StoryWriter.toGet("Calculation",
                calculator1 -> calculator1.get(calculation).floatValue() + 5F));
        try {
            calculator.get(Arithmetical.divide(0F));
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
        DoCapturesOf.DO_CAPTURES_OF_INSTANCE.accept(CapturedEvents.SUCCESS.name());
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    contains("Getting of 'Entering number 9' succeed. The result 9.0",
                            "Getting of 'Divide by number -6' succeed. The result -1.5",
                            "Value that was used to get a result from 9.0",
                            "Getting of 'Multiplying by number 11' succeed. The result -16.5",
                            "Value that was used to get a result from -1.5",
                            "Getting of 'Divide by number 0.5' succeed. The result -33.0",
                            "Value that was used to get a result from -16.5",
                            "Getting of 'Subtract number 100' succeed. The result -133.0",
                            "Value that was used to get a result from -33.0",
                            "Getting of 'Calculation' succeed. The result -128.0"));

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains("Performing of 'Reset calculated value to 0' succeed Calculator",
                            "Getting of 'Entering number 9' succeed. The result 9.0",
                            "Value that was used to get a result from Calculator",
                            "Getting of 'Divide by number -6' succeed. The result -1.5",
                            "Value that was used to get a result from 9.0",
                            "Getting of 'Multiplying by number 11' succeed. The result -16.5",
                            "Value that was used to get a result from -1.5",
                            "Getting of 'Divide by number 0.5' succeed. The result -33.0",
                            "Value that was used to get a result from -16.5",
                            "Getting of 'Subtract number 100' succeed. The result -133.0",
                            "Value that was used to get a result from -33.0",
                            "Getting of 'Calculation' succeed. The result -128.0",
                            "Value that was used to get a result from Calculator"));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        }
        finally {
            getProperties().remove(DoCapturesOf.DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void captorTestWhenTypeOfEventIsFailure() {
        DoCapturesOf.DO_CAPTURES_OF_INSTANCE.accept(CapturedEvents.FAILURE.name());
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    emptyIterable());

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains("Getting of 'Divide by number 0.0' failed. Value that was used to get a result from Calculator"));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        }
        finally {
            getProperties().remove(DoCapturesOf.DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void captorTestWhenTypeOfEventIsAll() {
        DoCapturesOf.DO_CAPTURES_OF_INSTANCE.accept(CapturedEvents.SUCCESS_AND_FAILURE.name());
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    contains("Getting of 'Entering number 9' succeed. The result 9.0",
                            "Getting of 'Divide by number -6' succeed. The result -1.5",
                            "Value that was used to get a result from 9.0",
                            "Getting of 'Multiplying by number 11' succeed. The result -16.5",
                            "Value that was used to get a result from -1.5",
                            "Getting of 'Divide by number 0.5' succeed. The result -33.0",
                            "Value that was used to get a result from -16.5",
                            "Getting of 'Subtract number 100' succeed. The result -133.0",
                            "Value that was used to get a result from -33.0",
                            "Getting of 'Calculation' succeed. The result -128.0"));

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains("Performing of 'Reset calculated value to 0' succeed Calculator",
                            "Getting of 'Entering number 9' succeed. The result 9.0",
                            "Value that was used to get a result from Calculator",
                            "Getting of 'Divide by number -6' succeed. The result -1.5",
                            "Value that was used to get a result from 9.0",
                            "Getting of 'Multiplying by number 11' succeed. The result -16.5",
                            "Value that was used to get a result from -1.5",
                            "Getting of 'Divide by number 0.5' succeed. The result -33.0",
                            "Value that was used to get a result from -16.5",
                            "Getting of 'Subtract number 100' succeed. The result -133.0",
                            "Value that was used to get a result from -33.0",
                            "Getting of 'Calculation' succeed. The result -128.0",
                            "Value that was used to get a result from Calculator",
                            "Getting of 'Divide by number 0.0' failed. Value that was used to get a result from Calculator"));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        }
        finally {
            getProperties().remove(DoCapturesOf.DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void eventFiringTest() {
        prepare();
        assertThat(TestEventLogger.MESSAGES, contains("Reset calculated value to 0 has started",
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
