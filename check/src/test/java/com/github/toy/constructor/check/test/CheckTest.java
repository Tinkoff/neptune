package com.github.toy.constructor.check.test;

import com.github.toy.constructor.check.Check;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.github.toy.constructor.check.Value.value;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.Substitution.getSubstituted;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.FAILURE;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.SUCCESS;
import static com.github.toy.constructor.core.api.properties.CapturedEvents.SUCCESS_AND_FAILURE;
import static com.github.toy.constructor.core.api.properties.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.getProperties;
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
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsNotDefined() {
        check.verify(value(4)
                .expected("Is integer", number ->
                        Integer.class.isAssignableFrom(number.getClass()),
                        is(true))
                .expected("Sqrt value", number -> sqrt(number.doubleValue()), is(2D)))

                .verify(value(5).expected("Sqr value",
                        number -> pow(number.doubleValue(), 2), is(25D)));

        assertThat("Logged messages",
                DefaultListLogger.messages,
                emptyIterable());
    }


    @Test
    public void testOfLinearPositiveCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.verify(value(4)
                    .expected("Is integer", number ->
                            Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .expected("Sqrt value", number -> sqrt(number.doubleValue()),
                            is(2D)))

                    .verify(value(5).expected("Sqr value",
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
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.verify(value(4)
                    .expected("Is integer", number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .expected("Sqrt value", number -> sqrt(number.doubleValue()),
                            is(2D)))

                    .verify(value(5).expected("Sqr value",
                            number -> pow(number.doubleValue(), 2),
                            is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    emptyIterable());
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.verify(value(4)
                    .expected("Is integer", number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .expected("Sqrt value", number -> sqrt(number.doubleValue()),
                            is(2D)))

                    .verify(value(5).expected("Sqr value",
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
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsNotDefined() {
        check.perform(action("Check numbers 4 and 5",
                check -> check.verify(value(4)
                        .expected("Is integer", number ->
                                        Integer.class.isAssignableFrom(number.getClass()),
                                is(true))

                        .expected("Sqrt value", number -> sqrt(number.doubleValue()),
                                is(2D)))

                        .verify(value(5).expected("Sqr value",
                                number -> pow(number.doubleValue(), 2),
                                is(25D)))));

        assertThat("Logged messages",
                DefaultListLogger.messages,
                emptyIterable());
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.perform(action("Check numbers 4 and 5",
                    check -> check.verify(value(4)
                            .expected("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .expected("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(value(5).expected("Sqr value",
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
                    check -> check.verify(value(4)
                            .expected("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .expected("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(value(5).expected("Sqr value",
                                    number -> pow(number.doubleValue(), 2),
                                    is(25D)))));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    emptyIterable());
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
                    check -> check.verify(value(4)
                            .expected("Is integer", number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .expected("Sqrt value", number -> sqrt(number.doubleValue()),
                                    is(2D)))

                            .verify(value(5).expected("Sqr value",
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsNotDefined() {
        try {
            check.verify(value(9)
                    .expected("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .expected("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)));
        }
        finally {
            assertThat(DefaultListLogger.messages,
                    emptyIterable());
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.verify(value(9)
                    .expected("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .expected("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Getting of 'Inspected value 9' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed"));
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.verify(value(9)
                    .expected("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .expected("Sqrt value",
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.verify(value(9)
                    .expected("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                    .expected("Sqrt value",
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsNotDefined() {
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(value(9)
                            .expected("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .expected("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))));
        }
        finally {
            assertThat(DefaultListLogger.messages,
                    emptyIterable());
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(value(9)
                            .expected("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .expected("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Getting of 'Inspected value 9' succeed",
                            "Getting of 'Is integer' succeed",
                            "Getting of 'Sqrt value' succeed"));
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(value(9)
                            .expected("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .expected("Sqrt value",
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
                    "             It was expected value Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsAll() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.verify(value(9)
                            .expected("Is integer",
                                    number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                            .expected("Sqrt value",
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
        }
    }
}
