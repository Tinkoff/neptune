package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents;
import ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf;
import ru.tinkoff.qa.neptune.core.api.steps.context.ConstructorParameters;
import ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory;

import java.util.function.Function;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Arithmetical.divide;
import static ru.tinkoff.qa.neptune.core.api.steps.Arithmetical.number;
import static ru.tinkoff.qa.neptune.core.api.steps.ArithmeticalSequence.divideByResultOf;
import static ru.tinkoff.qa.neptune.core.api.steps.ArithmeticalSequence.multiplyByResultOf;
import static ru.tinkoff.qa.neptune.core.api.steps.ArithmeticalSequence.subtractFromResultOf;
import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;

public class EventFiringTest {

    private CalculatorSteps calculator;

    private static final SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> calculation =
            subtractFromResultOf(100,
                    divideByResultOf(0.5,
                            multiplyByResultOf(11,
                                    divideByResultOf(-6,
                                            number(9)))));
    private static boolean toReport = true;

    private static final StepFunction<CalculatorSteps, Number> CALCULATION = toGet("Calculation",
            (Function<CalculatorSteps, Number>) calculatorSteps -> {
                var f = ((StepFunction<CalculatorSteps, Number>) calculation.get());
                if (toReport) {
                f = f.turnReportingOn();
                } else {
                    f = f.turnReportingOff();
                }
                return calculatorSteps.get(f).floatValue() + 5F;
            })

            .onFinishMakeCaptureOfType(Number.class);

    @BeforeClass
    public void beforeAll() {
        calculator = ProxyFactory.getProxied(CalculatorSteps.class, ConstructorParameters.params());
    }

    private void prepare() {
        TestCaptor.messages.clear();
        TestCapturedStringInjector.messages.clear();
        TestCapturedFileInjector.messages.clear();
        TestNumberCaptor.numbers.clear();
        TestEventLogger.MESSAGES.clear();

        calculator.reset();
        calculator.get(CALCULATION);
        try {
            var divideByZero = (StepFunction<CalculatorSteps, Number>) divide(0F).get();
            if (toReport) {
                divideByZero = divideByZero.turnReportingOn();
            } else {
                divideByZero = divideByZero.turnReportingOff();
            }
            calculator.get(divideByZero);
        } catch (Throwable t) {
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
                    contains("Value  9.0",
                            "Value  -1.5",
                            "Value  -16.5",
                            "Value  -33.0",
                            "Value  -133.0"));

            assertThat("Check messages logged by SPI Number logger",
                    TestNumberCaptor.numbers,
                    contains(-128F));

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains("Saved to string 9.0",
                            "Saved to string -1.5",
                            "Saved to string -16.5",
                            "Saved to string -33.0",
                            "Saved to string -133.0"));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        } finally {
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

            assertThat("Check messages logged by SPI Number logger",
                    TestNumberCaptor.numbers,
                    emptyIterable());

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains(containsString("Saved to string ru.tinkoff.qa.neptune.core.api.steps.CalculatorSteps")));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        } finally {
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
                    contains("Value  9.0",
                            "Value  -1.5",
                            "Value  -16.5",
                            "Value  -33.0",
                            "Value  -133.0"));

            assertThat("Check messages logged by SPI Number logger",
                    TestNumberCaptor.numbers,
                    contains(-128F));

            assertThat("Check messages logged by SPI String logger",
                    TestCapturedStringInjector.messages,
                    contains(equalTo("Saved to string 9.0"),
                            equalTo("Saved to string -1.5"),
                            equalTo("Saved to string -16.5"),
                            equalTo("Saved to string -33.0"),
                            equalTo("Saved to string -133.0"),
                            containsString("Saved to string ru.tinkoff.qa.neptune.core.api.steps.CalculatorSteps")));

            assertThat("Check messages logged by SPI File logger",
                    TestCapturedFileInjector.messages,
                    emptyIterable());
        } finally {
            getProperties().remove(DoCapturesOf.DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void eventFiringTest() {
        prepare();
        assertThat(TestEventLogger.MESSAGES, contains("Reset calculated value to 0 has started",
                "Event finished",
                "Get Calculation has started",
                "Get Subtraction of number 100. From Divide by number 0.5 has started",
                "Get Entering number 9 has started",
                "9.0 has been returned",
                "Event finished",
                "Get Divide by number -6. From Entering number 9 has started",
                "-1.5 has been returned",
                "Event finished",
                "Get Multiplying by number 11. From Divide by number -6 has started",
                "-16.5 has been returned",
                "Event finished",
                "Get Divide by number 0.5. From Multiplying by number 11 has started",
                "-33.0 has been returned",
                "Event finished",
                "Get Subtraction of number 100. From Divide by number 0.5 has started",
                "-133.0 has been returned",
                "Event finished",
                "-133.0 has been returned",
                "Event finished",
                "-128.0 has been returned",
                "Event finished",
                "Get Divide by number 0.0 has started",
                "java.lang.ArithmeticException has been thrown",
                "Event finished"));
    }

    @Test()
    public void turnOffReportingTest() {
        toReport = false;
        try {
            prepare();
            assertThat(TestEventLogger.MESSAGES, contains("Reset calculated value to 0 has started",
                    "Event finished",
                    "Get Calculation has started",
                    "-128.0 has been returned",
                    "Event finished"));
        }
        finally {
            toReport = true;
        }
    }
}
