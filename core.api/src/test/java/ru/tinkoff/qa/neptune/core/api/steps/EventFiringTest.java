package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf;

import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.ToLimitReportDepth.TO_LIMIT_REPORT_DEPTH_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.steps.Arithmetical.divide;
import static ru.tinkoff.qa.neptune.core.api.steps.Arithmetical.number;
import static ru.tinkoff.qa.neptune.core.api.steps.ArithmeticalSequence.*;
import static ru.tinkoff.qa.neptune.core.api.steps.CalculatorSteps.calculator;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class EventFiringTest {

    private static final SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> CALCULATION =
            subtractFromResultOf(100,
                    divideByResultOf(0.5,
                            multiplyByResultOf(11,
                                    divideByResultOf(-6,
                                            number(9)))));
    private static boolean toReport = true;

    private void prepare() {
        TestCaptor.messages.clear();
        TestCapturedStringInjector.messages.clear();
        TestCapturedFileInjector.messages.clear();
        TestNumberCaptor.numbers.clear();
        TestEventLogger.MESSAGES.clear();

        $("Reset calculated value to 0", () ->
                calculator().set(0D));

        if (toReport) {
            $("Result of numeric operations", () -> {
                var result = calculator().evaluate(CALCULATION);
                return result.floatValue() + 5F;
            });
        }
        else {
            var f = ((Get<CalculatorSteps, Number>) CALCULATION.get());
            var func = f.turnReportingOff();
            $("Result of numeric operations", () -> {
                var result = calculator().evaluate(func);
                return result.floatValue() + 5F;
            });
        }

        try {
            var divideByZero = (Get<CalculatorSteps, Number>) divide(0F).get();
            $("Result of the dividing by zero", () ->
                    calculator().evaluate(divideByZero));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @AfterMethod
    @BeforeMethod
    public void prepareAndRevert() {
        toReport = true;
        TO_LIMIT_REPORT_DEPTH_PROPERTY.accept(false);
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
        DoCapturesOf.DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    contains("Value  -1.5",
                            "Value  -16.5",
                            "Value  -33.0",
                            "Value  -133.0"));

            assertThat("Check messages logged by SPI Number logger",
                    TestNumberCaptor.numbers,
                    contains(9.0,
                            -1.5,
                            -16.5,
                            -33.0,
                            -133.0));

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
            getProperties().remove(DoCapturesOf.DO_CAPTURES_OF_INSTANCE.getName());
        }
    }

    @Test
    public void captorTestWhenTypeOfEventIsFailure() {
        DoCapturesOf.DO_CAPTURES_OF_INSTANCE.accept(FAILURE);
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
            getProperties().remove(DoCapturesOf.DO_CAPTURES_OF_INSTANCE.getName());
        }
    }

    @Test
    public void captorTestWhenTypeOfEventIsAll() {
        DoCapturesOf.DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
        prepare();
        try {
            assertThat("Check messages logged by SPI logger",
                    TestCaptor.messages,
                    contains("Value  -1.5",
                            "Value  -16.5",
                            "Value  -33.0",
                            "Value  -133.0"));

            assertThat("Check messages logged by SPI Number logger",
                    TestNumberCaptor.numbers,
                    contains(9.0,
                            -1.5,
                            -16.5,
                            -33.0,
                            -133.0));

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
            getProperties().remove(DoCapturesOf.DO_CAPTURES_OF_INSTANCE.getName());
        }
    }

    @Test
    public void eventFiringTest() {
        prepare();
        assertThat(TestEventLogger.MESSAGES, contains("Reset calculated value to 0 has started",
                "Event finished",
                "Result of numeric operations has started",
                "Get: Subtraction of number 100 has started",
                "Get: Divide by number 0.5 has started",
                "Get: Multiplying by number 11 has started",
                "Get: Divide by number -6 has started",
                "Get: Entering number 9 has started",
                "Result: 9.0 has been returned",
                "Event finished",
                "Result: -1.5 has been returned",
                "Event finished",
                "Result: -16.5 has been returned",
                "Event finished",
                "Result: -33.0 has been returned",
                "Event finished",
                "Result: -133.0 has been returned",
                "Event finished",
                "Result: -128.0 has been returned",
                "Event finished",
                "Result of the dividing by zero has started",
                "Get: Divide by number 0.0 has started",
                "java.lang.ArithmeticException has been thrown",
                "Event finished",
                "java.lang.ArithmeticException has been thrown",
                "Event finished"));
    }

    @Test()
    public void turnOffReportingTest() {
        toReport = false;
        prepare();
        assertThat(TestEventLogger.MESSAGES, contains("Reset calculated value to 0 has started",
                "Event finished",
                "Result of numeric operations has started",
                "Result: -128.0 has been returned",
                "Event finished",
                "Result of the dividing by zero has started",
                "Get: Divide by number 0.0 has started",
                "java.lang.ArithmeticException has been thrown",
                "Event finished",
                "java.lang.ArithmeticException has been thrown",
                "Event finished"));
    }

    @Test
    public void eventFiringWithLimits() {
        TO_LIMIT_REPORT_DEPTH_PROPERTY.accept(true);
        prepare();
        assertThat(TestEventLogger.MESSAGES, contains("Reset calculated value to 0 has started",
                "Event finished",
                "Result of numeric operations has started",
                "Get: Subtraction of number 100 has started",
                "Get: Divide by number 0.5 has started",
                "Result: -33.0 has been returned",
                "Event finished",
                "Result: -133.0 has been returned",
                "Event finished",
                "Result: -128.0 has been returned",
                "Event finished",
                "Result of the dividing by zero has started",
                "Get: Divide by number 0.0 has started",
                "java.lang.ArithmeticException has been thrown",
                "Event finished",
                "java.lang.ArithmeticException has been thrown",
                "Event finished"));
    }
}