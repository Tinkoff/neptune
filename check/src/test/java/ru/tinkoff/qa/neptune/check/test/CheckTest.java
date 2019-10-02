package ru.tinkoff.qa.neptune.check.test;

import ru.tinkoff.qa.neptune.check.CheckStepContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
import static ru.tinkoff.qa.neptune.check.test.TestEventLogger.MESSAGES;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.getProperties;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CheckTest {

    private CheckStepContext check;

    @BeforeTest
    public void beforeTest() {
        check = getProxied(CheckStepContext.class);
    }

    @BeforeMethod
    public void beforeMethod() {
        DefaultListLogger.messages.clear();
        MESSAGES.clear();
    }

    private static final List<String> EXPECTED_LOGGER_MESSAGES1 =
            of("Verify inspected value 4 has started",
                    "Check Is integer. Assert: is <true> has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Assert is <true> has started",
                    "Event finished",
                    "Event finished",
                    "Check Sqrt value. Assert: is <2.0> has started",
                    "Get Sqrt value has started",
                    "2.0 has been returned",
                    "Event finished",
                    "Assert is <2.0> has started",
                    "Event finished",
                    "Event finished",
                    "Event finished",
                    "Verify inspected value 5 has started",
                    "Check Sqr value. Assert: is <25.0> has started",
                    "Get Sqr value has started",
                    "25.0 has been returned",
                    "Event finished",
                    "Assert is <25.0> has started",
                    "Event finished",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES2 =
            of("Check numbers 4 and 5 has started",
                    "Verify inspected value 4 has started",
                    "Check Is integer. Assert: is <true> has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Assert is <true> has started",
                    "Event finished",
                    "Event finished",
                    "Check Sqrt value. Assert: is <2.0> has started",
                    "Get Sqrt value has started",
                    "2.0 has been returned",
                    "Event finished",
                    "Assert is <2.0> has started",
                    "Event finished",
                    "Event finished",
                    "Event finished",
                    "Verify inspected value 5 has started",
                    "Check Sqr value. Assert: is <25.0> has started",
                    "Get Sqr value has started",
                    "25.0 has been returned",
                    "Event finished",
                    "Assert is <25.0> has started",
                    "Event finished",
                    "Event finished",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES3 =
            of("Verify inspected value 9 has started",
                    "Check Is integer. Assert: is <true> has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Assert is <true> has started",
                    "Event finished",
                    "Event finished",
                    "Check Sqrt value. Assert: is <2.0> has started",
                    "Get Sqrt value has started",
                    "3.0 has been returned",
                    "Event finished",
                    "Assert is <2.0> has started",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES4 =
            of("Check number 9 has started",
                    "Verify inspected value 9 has started",
                    "Check Is integer. Assert: is <true> has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Assert is <true> has started",
                    "Event finished",
                    "Event finished",
                    "Check Sqrt value. Assert: is <2.0> has started",
                    "Get Sqrt value has started",
                    "3.0 has been returned",
                    "Event finished",
                    "Assert is <2.0> has started",
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
                    contains("Value true",
                            "Value true",
                            "Value 4",
                            "Value 2.0",
                            "Value 2.0",
                            "Value 4",
                            "Value 25.0",
                            "Value 25.0",
                            "Value 5"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
        } finally {
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
        } finally {
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
                    contains("Value true",
                            "Value true",
                            "Value 4",
                            "Value 2.0",
                            "Value 2.0",
                            "Value 4",
                            "Value 25.0",
                            "Value 25.0",
                            "Value 5"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsNotDefined() {
        check.perform("Check numbers 4 and 5",
                check -> check.verify(thatValue(4)
                        .suitsCriteria("Is integer", number ->
                                        Integer.class.isAssignableFrom(number.getClass()),
                                is(true))

                        .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                is(2D)))

                        .verify(thatValue(5).suitsCriteria("Sqr value",
                                number -> pow(number.doubleValue(), 2),
                                is(25D))));

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
            check.perform("Check numbers 4 and 5",
                    check -> check.verify(thatValue(4)
                            .suitsCriteria("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(thatValue(5).suitsCriteria("Sqr value",
                                    number -> pow(number.doubleValue(), 2),
                                    is(25D))));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Value true",
                            "Value true",
                            "Value 4",
                            "Value 2.0",
                            "Value 2.0",
                            "Value 4",
                            "Value 25.0",
                            "Value 25.0",
                            "Value 5"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.perform("Check numbers 4 and 5",
                    check -> check.verify(thatValue(4)
                            .suitsCriteria("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(thatValue(5).suitsCriteria("Sqr value",
                                    number -> pow(number.doubleValue(), 2),
                                    is(25D))));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.perform("Check numbers 4 and 5",
                    check -> check.verify(thatValue(4)
                            .suitsCriteria("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(thatValue(5).suitsCriteria("Sqr value",
                                    number -> pow(number.doubleValue(), 2),
                                    is(25D))));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Value true",
                            "Value true",
                            "Value 4",
                            "Value 2.0",
                            "Value 2.0",
                            "Value 4",
                            "Value 25.0",
                            "Value 25.0",
                            "Value 5"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
            "*['Expected: is <2.0>']" +
            "*['but: Sqrt value:']" +
            "*['was <3.0>']")
    public void testOfLinearNegativeCaseWhenEventIsNotDefined() {
        try {
            check.verify(thatValue(9)
                    .suitsCriteria("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .suitsCriteria("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        } finally {
            assertThat(DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
            "*['Expected: is <2.0>']" +
            "*['but: Sqrt value:']" +
            "*['was <3.0>']")
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
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value true",
                            "Value true",
                            "Value 9",
                            "Value 3.0"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = ".*[List of mismatches:]" +
                    "*['Expected: is <2.0>']" +
                    "*['but: Sqrt value:']" +
                    "*['was <3.0>']")
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
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value 3.0",
                            "Value 9"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
                    "*['Expected: is <2.0>']" +
                    "*['but: Sqrt value:']" +
                    "*['was <3.0>']")
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
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value true",
                            "Value true",
                            "Value 9",
                            "Value 3.0",
                            "Value 3.0",
                            "Value 9"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
                    "*['Expected: is <2.0>']" +
                    "*['but: Sqrt value:']" +
                    "*['was <3.0>']")
    public void testOfPerformNegativeCaseWhenEventIsNotDefined() {
        try {
            check.perform("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D))));
        } finally {
            assertThat(DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = ".*[List of mismatches:']" +
                    "*['Expected: is <2.0>']" +
                    "*['but: Sqrt value:']" +
                    "*['was <3.0>']")
    public void testOfPerformNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.perform("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D))));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value true",
                            "Value true",
                            "Value 9",
                            "Value 3.0"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
                    "*['Expected: is <2.0>']" +
                    "*['but: Sqrt value:']" +
                    "*['was <3.0>']")
    public void testOfPerformNegativeCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.perform("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D))));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value 3.0",
                            "Value 9"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
                    "*['Expected: is <2.0>']" +
                    "*['but: Sqrt value:']" +
                    "*['was <3.0>']")
    public void testOfPerformNegativeCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.perform("Check number 9", check ->
                    check.verify(thatValue(9)
                            .suitsCriteria("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .suitsCriteria("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D))));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value true",
                            "Value true",
                            "Value 9",
                            "Value 3.0",
                            "Value 3.0",
                            "Value 9"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test
    public void testOfNullValue() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        check.verify(thatValue("Given value", null)
                .suitsCriteria(nullValue()));

        assertThat(MESSAGES,
                contains("Verify Given value has started",
                        "Assert null has started",
                        "Event finished",
                        "Event finished"));
    }
}
