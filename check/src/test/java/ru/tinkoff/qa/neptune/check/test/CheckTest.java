package ru.tinkoff.qa.neptune.check.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.getProperties;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.test.TestEventLogger.MESSAGES;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

public class CheckTest {

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
                    "Event finished",
                    "Check Sqrt value. Assert: is <2.0> has started",
                    "Get Sqrt value has started",
                    "2.0 has been returned",
                    "Event finished",
                    "Event finished",
                    "Event finished",
                    "Verify inspected value 5 has started",
                    "Check Sqr value. Assert: is <25.0> has started",
                    "Get Sqr value has started",
                    "25.0 has been returned",
                    "Event finished",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES2 =
            of("Verify inspected value 9 has started",
                    "Check Is integer. Assert: is <true> has started",
                    "Get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "Event finished",
                    "Check Sqrt value. Assert: is <2.0> has started",
                    "Get Sqrt value has started",
                    "3.0 has been returned",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished");

    @Test
    public void testOfLinearPositiveCaseWhenEventIsNotDefined() {
        check(4, match("Is integer", number ->
                        Integer.class.isAssignableFrom(number.getClass()),
                is(true)),
                match("Sqrt value", number -> sqrt(number.doubleValue()), is(2D)));

        check(5, match("Sqr value", number -> pow(number.doubleValue(), 2), is(25D)));

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
            check(4,
                    match("Is integer", number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value", number -> sqrt(number.doubleValue()),
                            is(2D)));

            check(5, match("Sqr value",
                    number -> pow(number.doubleValue(), 2),
                    is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Value true",
                            "Value 4",
                            "Value 2.0",
                            "Value 4",
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
            check(4,
                    match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));

            check(5, match("Sqr value",
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
            check(4,
                    match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));

            check(5, match("Sqr value",
                    number -> pow(number.doubleValue(), 2),
                    is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Value true",
                            "Value 4",
                            "Value 2.0",
                            "Value 4",
                            "Value 25.0",
                            "Value 5"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
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
            check(9,
                    match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        } finally {
            assertThat(DefaultListLogger.messages,
                    emptyIterable());

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
            "*['Expected: is <2.0>']" +
            "*['but: Sqrt value:']" +
            "*['was <3.0>']")
    public void testOfLinearNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check(9,
                    match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value true",
                            "Value 9",
                            "Value 3.0"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
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
            check(9,
                    match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value 9"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
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
            check(9, match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        } finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Value true",
                            "Value 9",
                            "Value 3.0",
                            "Value 9"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        }
    }

    @Test
    public void testOfNullValue() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        Object obj = null;
        check("Given value", obj, match(nullValue()));

        assertThat(MESSAGES,
                contains("Verify Given value has started",
                        "Check object. Assert: null has started",
                        "Event finished",
                        "Event finished"));
    }
}
