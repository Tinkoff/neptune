package ru.tinkoff.qa.neptune.check.test;

import ru.tinkoff.qa.neptune.check.Check;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
import static ru.tinkoff.qa.neptune.check.test.TestEventLogger.MESSAGES;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.action;
import static ru.tinkoff.qa.neptune.core.api.proxy.ProxyFactory.getProxied;
import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.getProperties;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CheckTest {

    private Check check;

    @BeforeTest
    public void beforeTest() {
        check = getProxied(Check.class);
    }

    @BeforeMethod
    public void beforeMethod() {
        DefaultListLogger.messages.clear();
        MESSAGES.clear();
    }

    private static final List<String> EXPECTED_LOGGER_MESSAGES1 =
            of("Verify inspected value 4 has started",
                    "Assertion. Target: Is integer. Action parameters: {is <true>} has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Attempt to verify. Target: true has started",
                    "Event finished",
                    "Event finished",
                    "Assertion. Target: Sqrt value. Action parameters: {is <2.0>} has started",
                    "Get Sqrt value has started",
                    "2.0 has been returned",
                    "Event finished",
                    "Attempt to verify. Target: 2.0 has started",
                    "Event finished",
                    "Event finished",
                    "Event finished",
                    "Verify inspected value 5 has started",
                    "Assertion. Target: Sqr value. Action parameters: {is <25.0>} has started",
                    "Get Sqr value has started",
                    "25.0 has been returned",
                    "Event finished",
                    "Attempt to verify. Target: 25.0 has started",
                    "Event finished",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES2 =
            of("Check numbers 4 and 5 has started",
                    "Verify inspected value 4 has started",
                    "Assertion. Target: Is integer. Action parameters: {is <true>} has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Attempt to verify. Target: true has started",
                    "Event finished",
                    "Event finished",
                    "Assertion. Target: Sqrt value. Action parameters: {is <2.0>} has started",
                    "Get Sqrt value has started",
                    "2.0 has been returned",
                    "Event finished",
                    "Attempt to verify. Target: 2.0 has started",
                    "Event finished",
                    "Event finished",
                    "Event finished",
                    "Verify inspected value 5 has started",
                    "Assertion. Target: Sqr value. Action parameters: {is <25.0>} has started",
                    "Get Sqr value has started",
                    "25.0 has been returned",
                    "Event finished",
                    "Attempt to verify. Target: 25.0 has started",
                    "Event finished",
                    "Event finished",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES3 =
            of("Verify inspected value 9 has started",
                    "Assertion. Target: Is integer. Action parameters: {is <true>} has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Attempt to verify. Target: true has started",
                    "Event finished",
                    "Event finished",
                    "Assertion. Target: Sqrt value. Action parameters: {is <2.0>} has started",
                    "Get Sqrt value has started",
                    "3.0 has been returned",
                    "Event finished",
                    "Attempt to verify. Target: 3.0 has started",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES4 =
            of("Check number 9 has started",
                    "Verify inspected value 9 has started",
                    "Assertion. Target: Is integer. Action parameters: {is <true>} has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Attempt to verify. Target: true has started",
                    "Event finished",
                    "Event finished",
                    "Assertion. Target: Sqrt value. Action parameters: {is <2.0>} has started",
                    "Get Sqrt value has started",
                    "3.0 has been returned",
                    "Event finished",
                    "Attempt to verify. Target: 3.0 has started",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished");

    @Test
    public void testOfLinearPositiveCaseWhenEventIsNotDefined() {
        check.verify(thatValue(4)
                .suitsCriteria("Is integer", number ->
                        Integer.class.isAssignableFrom(number.getClass()),
                        is(true))
                .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()), is(2D)))

                .verify(thatValue(5).suitsCriteria("Sqr value",
                        number -> pow(number.doubleValue(), 2), is(25D)));

        assertThat("Logged messages",
                DefaultListLogger.messages,
                emptyIterable());

        assertThat(MESSAGES,
                contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
    }


    @Test
    public void testOfLinearPositiveCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.verify(thatValue(4)
                    .suitsCriteria("Is integer", number ->
                            Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                            is(2D)))

                    .verify(thatValue(5).suitsCriteria("Sqr value",
                            number -> pow(number.doubleValue(), 2),
                            is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' succeed",
                            "Performing of 'Verify inspected value 4' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqr value. Action parameters: {is <25.0>}' succeed",
                            "Performing of 'Verify inspected value 5' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.verify(thatValue(4)
                    .suitsCriteria("Is integer", number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                            is(2D)))

                    .verify(thatValue(5).suitsCriteria("Sqr value",
                            number -> pow(number.doubleValue(), 2),
                            is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.verify(thatValue(4)
                    .suitsCriteria("Is integer", number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                            is(2D)))

                    .verify(thatValue(5).suitsCriteria("Sqr value",
                            number -> pow(number.doubleValue(), 2),
                            is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' succeed",
                            "Performing of 'Verify inspected value 4' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqr value. Action parameters: {is <25.0>}' succeed",
                            "Performing of 'Verify inspected value 5' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsNotDefined() {
        check.perform(action("Check numbers 4 and 5",
                check -> check.verify(thatValue(4)
                        .suitsCriteria("Is integer", number ->
                                        Integer.class.isAssignableFrom(number.getClass()),
                                is(true))

                        .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                is(2D)))

                        .verify(thatValue(5).suitsCriteria("Sqr value",
                                number -> pow(number.doubleValue(), 2),
                                is(25D)))));

        assertThat("Logged messages",
                DefaultListLogger.messages,
                emptyIterable());

        assertThat(MESSAGES,
                contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.perform(action("Check numbers 4 and 5",
                    check -> check.verify(thatValue(4)
                            .suitsCriteria("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(thatValue(5).suitsCriteria("Sqr value",
                                    number -> pow(number.doubleValue(), 2),
                                    is(25D)))));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' succeed",
                            "Performing of 'Verify inspected value 4' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqr value. Action parameters: {is <25.0>}' succeed",
                            "Performing of 'Verify inspected value 5' succeed",
                            "Performing of 'Check numbers 4 and 5' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.perform(action("Check numbers 4 and 5",
                    check -> check.verify(thatValue(4)
                            .suitsCriteria("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(thatValue(5).suitsCriteria("Sqr value",
                                    number -> pow(number.doubleValue(), 2),
                                    is(25D)))));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.perform(action("Check numbers 4 and 5",
                    check -> check.verify(thatValue(4)
                            .suitsCriteria("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(thatValue(5).suitsCriteria("Sqr value",
                                    number -> pow(number.doubleValue(), 2),
                                    is(25D)))));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' succeed",
                            "Performing of 'Verify inspected value 4' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Sqr value. Action parameters: {is <25.0>}' succeed",
                            "Performing of 'Verify inspected value 5' succeed",
                            "Performing of 'Check numbers 4 and 5' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "List of mismatches:\n" +
            "\t\n" +
            "Expected: is <2.0>\n" +
            "     but: Sqrt value: \n" +
            "\twas <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsNotDefined() {
        try {
            check.verify(thatValue(9)
                    .suitsCriteria("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)));
        }
        finally {
            assertThat(DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "List of mismatches:\n" +
            "\t\n" +
            "Expected: is <2.0>\n" +
            "     but: Sqrt value: \n" +
            "\twas <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.verify(thatValue(9)
                    .suitsCriteria("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        }
        catch (AssertionError e) {
            System.out.println(e.getMessage());
            throw e;
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "List of mismatches:\n" +
                    "\t\n" +
                    "Expected: is <2.0>\n" +
                    "     but: Sqrt value: \n" +
                    "\twas <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.verify(thatValue(9)
                    .suitsCriteria("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Performing of 'Attempt to verify' failed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' failed",
                            "Performing of 'Verify inspected value 9' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "List of mismatches:\n" +
                    "\t\n" +
                    "Expected: is <2.0>\n" +
                    "     but: Sqrt value: \n" +
                    "\twas <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.verify(thatValue(9)
                    .suitsCriteria("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Attempt to verify' failed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' failed",
                            "Performing of 'Verify inspected value 9' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "List of mismatches:\n" +
                    "\t\n" +
                    "Expected: is <2.0>\n" +
                    "     but: Sqrt value: \n" +
                    "\twas <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsNotDefined() {
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))));
        }
        finally {
            assertThat(DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "List of mismatches:\n" +
                    "\t\n" +
                    "Expected: is <2.0>\n" +
                    "     but: Sqrt value: \n" +
                    "\twas <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "List of mismatches:\n" +
                    "\t\n" +
                    "Expected: is <2.0>\n" +
                    "     but: Sqrt value: \n" +
                    "\twas <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Performing of 'Attempt to verify' failed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' failed",
                            "Performing of 'Verify inspected value 9' failed",
                            "Performing of 'Check number 9' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "List of mismatches:\n" +
                    "\t\n" +
                    "Expected: is <2.0>\n" +
                    "     but: Sqrt value: \n" +
                    "\twas <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Getting of 'Is integer' succeed",
                            "Performing of 'Attempt to verify' succeed",
                            "Performing of 'Assertion. Target: Is integer. Action parameters: {is <true>}' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Attempt to verify' failed",
                            "Performing of 'Assertion. Target: Sqrt value. Action parameters: {is <2.0>}' failed",
                            "Performing of 'Verify inspected value 9' failed",
                            "Performing of 'Check number 9' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }
}
