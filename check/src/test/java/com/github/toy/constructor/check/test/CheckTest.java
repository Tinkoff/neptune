package com.github.toy.constructor.check.test;

import com.github.toy.constructor.check.Check;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static com.github.toy.constructor.check.ThatValue.thatValue;
import static com.github.toy.constructor.check.test.TestEventLogger.MESSAGES;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.FAILURE;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.SUCCESS;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.SUCCESS_AND_FAILURE;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
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
        check = getSubstituted(Check.class);
    }

    @BeforeMethod
    public void beforeMethod() {
        DefaultListLogger.messages.clear();
        MESSAGES.clear();
    }

    private static final List<String> EXPECTED_LOGGER_MESSAGES1 =
            of("Perform Assert value . With parameters: {Inspected value 4,\n" +
                            "Is integer\n" +
                            "           is <true>\n" +
                            "Sqrt value\n" +
                            "           is <2.0>} has started",
                    "Get Inspected value 4 has started",
                    "4 has been returned",
                    "Event finished",
                    "From 4 get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "From 4 get Sqrt value has started",
                    "2.0 has been returned",
                    "Event finished",
                    "Event finished",
                    "Perform Assert value . With parameters: {Inspected value 5,\n" +
                            "Sqr value\n" +
                            "           is <25.0>} has started",
                    "Get Inspected value 5 has started",
                    "5 has been returned",
                    "Event finished",
                    "From 5 get Sqr value has started",
                    "25.0 has been returned",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES2 =
            of("Perform Check numbers 4 and 5 has started",
                    "Perform Assert value . With parameters: {Inspected value 4,\n" +
                            "Is integer\n" +
                            "           is <true>\n" +
                            "Sqrt value\n" +
                            "           is <2.0>} has started",
                    "Get Inspected value 4 has started",
                    "4 has been returned",
                    "Event finished",
                    "From 4 get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "From 4 get Sqrt value has started",
                    "2.0 has been returned",
                    "Event finished",
                    "Event finished",
                    "Perform Assert value . With parameters: {Inspected value 5,\n" +
                            "Sqr value\n" +
                            "           is <25.0>} has started",
                    "Get Inspected value 5 has started",
                    "5 has been returned",
                    "Event finished",
                    "From 5 get Sqr value has started",
                    "25.0 has been returned",
                    "Event finished",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES3 =
            of("Perform Assert value . With parameters: {Inspected value 9,\n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>} has started",
                    "Get Inspected value 9 has started",
                    "9 has been returned",
                    "Event finished",
                    "From 9 get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "From 9 get Sqrt value has started",
                    "3.0 has been returned",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES4 =
            of("Perform Check number 9 has started",
                    "Perform Assert value . With parameters: {Inspected value 9,\n" +
                            "Is integer\n" +
                            "           is <true>\n" +
                            "Sqrt value\n" +
                            "           is <2.0>} has started",
                    "Get Inspected value 9 has started",
                    "9 has been returned",
                    "Event finished",
                    "From 9 get Is integer has started",
                    "true has been returned",
                    "Event finished",
                    "From 9 get Sqrt value has started",
                    "3.0 has been returned",
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
                    contains("Getting of 'Inspected value 4' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 4,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' succeed",
                            "Getting of 'Inspected value 5' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 5,\n" +
                                    "Sqr value\n" +
                                    "           is <25.0>}' succeed"));

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
                    contains("Getting of 'Inspected value 4' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 4,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' succeed",
                            "Getting of 'Inspected value 5' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 5,\n" +
                                    "Sqr value\n" +
                                    "           is <25.0>}' succeed"));

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
                    contains("Getting of 'Inspected value 4' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 4,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' succeed",
                            "Getting of 'Inspected value 5' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 5,\n" +
                                    "Sqr value\n" +
                                    "           is <25.0>}' succeed",
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
                    contains("Getting of 'Inspected value 4' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 4,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' succeed",
                            "Getting of 'Inspected value 5' succeed",
                            "Getting of 'Sqr value' succeed",
                            "Performing of 'Assert value . With parameters: {Inspected value 5,\n" +
                                    "Sqr value\n" +
                                    "           is <25.0>}' succeed",
                            "Performing of 'Check numbers 4 and 5' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Getting of 'Inspected value 9' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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
                    contains("Mismatched object 9",
                            "Performing of 'Assert value . With parameters: {Inspected value 9,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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
                    contains("Getting of 'Inspected value 9' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Mismatched object 9",
                            "Performing of 'Assert value . With parameters: {Inspected value 9,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES3.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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
                    contains("Getting of 'Inspected value 9' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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
                    contains("Mismatched object 9",
                            "Performing of 'Assert value . With parameters: {Inspected value 9,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' failed",
                            "Performing of 'Check number 9' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "\n" +
                    "Expected:  \n" +
                    "Is integer\n" +
                    "           is <true>\n" +
                    "Sqrt value\n" +
                    "           is <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "Sqrt value:\n" +
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
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
                    contains("Getting of 'Inspected value 9' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed",
                            "Mismatched object 9",
                            "Performing of 'Assert value . With parameters: {Inspected value 9,\n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>}' failed",
                            "Performing of 'Check number 9' failed"));

            assertThat(MESSAGES,
                    contains(EXPECTED_LOGGER_MESSAGES4.toArray()));
        }
    }
}
