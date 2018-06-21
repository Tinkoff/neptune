package com.github.toy.constructor.check.test;

import com.github.toy.constructor.check.Check;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.function.Function;

import static com.github.toy.constructor.check.hamcrest.FluentMatcher.shouldMatch;
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
    public void beforeTest() throws Exception {
        check = getSubstituted(Check.class);
    }

    @BeforeMethod
    public void beforeMethod() {
        DefaultListLogger.messages.clear();
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsNotDefined() {
        check.assertThat("Check number 4", 4,
                shouldMatch("Is integer",
                        (Function<Integer, Boolean>) number ->
                                Integer.class.isAssignableFrom(number.getClass()),
                        is(true))

                        .and("Sqrt value",
                                number -> sqrt(number.doubleValue()),
                                is(2D)))

                .assertThat("Check number 4",
                        4,
                        is(4))

                .assertThat("Check number 5", 5,
                        shouldMatch("Sqr value",
                                integer -> pow(integer, 2),
                                is(25D)));

        assertThat("Logged messages",
                DefaultListLogger.messages,
                emptyIterable());
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.assertThat("Check number 4", 4,
                    shouldMatch("Is integer",
                            (Function<Integer, Boolean>) number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                            .and("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))

                    .assertThat("Check number 4",
                            4,
                            is(4))

                    .assertThat("Check number 5", 5,
                            shouldMatch("Sqr value",
                                    integer -> pow(integer, 2),
                                    is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Performing of 'Check number 4 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' succeed",
                            "Performing of 'Check number 4 by criteria is <4>' succeed",
                            "Performing of 'Check number 5 by criteria \n" +
                                    "Sqr value\n" +
                                    "           is <25.0>' succeed"));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfLinearPositiveCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.assertThat("Check number 4", 4,
                    shouldMatch("Is integer",
                            (Function<Integer, Boolean>) number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                            .and("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))

                    .assertThat("Check number 4",
                            4,
                            is(4))

                    .assertThat("Check number 5", 5,
                            shouldMatch("Sqr value",
                                    integer -> pow(integer, 2),
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
            check.assertThat("Check number 4", 4,
                    shouldMatch("Is integer",
                            (Function<Integer, Boolean>) number ->
                                    Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                            .and("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)))

                    .assertThat("Check number 4",
                            4,
                            is(4))

                    .assertThat("Check number 5", 5,
                            shouldMatch("Sqr value",
                                    integer -> pow(integer, 2),
                                    is(25D)));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Performing of 'Check number 4 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' succeed",
                            "Performing of 'Check number 4 by criteria is <4>' succeed",
                            "Performing of 'Check number 5 by criteria \n" +
                                    "Sqr value\n" +
                                    "           is <25.0>' succeed"));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
        }
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsNotDefined() {
        check.perform(action("Check numbers 4 and 5",
                check -> {
                    check.assertThat("Check number 4", 4,
                            shouldMatch("Is integer",
                                    (Function<Integer, Boolean>) number ->
                                            Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                                    .and("Sqrt value",
                                            number -> sqrt(number.doubleValue()),
                                            is(2D)));

                    check.assertThat("Check number 4",
                            4,
                            is(4));

                    check.assertThat("Check number 5", 5,
                            shouldMatch("Sqr value",
                                    integer -> pow(integer, 2),
                                    is(25D)));
                }));

        assertThat("Logged messages",
                DefaultListLogger.messages,
                emptyIterable());
    }

    @Test
    public void testOfPositivePerformCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.perform(action("Check numbers 4 and 5",
                    check -> {
                        check.assertThat("Check number 4", 4,
                                shouldMatch("Is integer",
                                        (Function<Integer, Boolean>) number ->
                                                Integer.class.isAssignableFrom(number.getClass()),
                                        is(true))

                                        .and("Sqrt value",
                                                number -> sqrt(number.doubleValue()),
                                                is(2D)));

                        check.assertThat("Check number 4",
                                4,
                                is(4));

                        check.assertThat("Check number 5", 5,
                                shouldMatch("Sqr value",
                                        integer -> pow(integer, 2),
                                        is(25D)));
                    }));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Performing of 'Check number 4 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' succeed",
                            "Performing of 'Check number 4 by criteria is <4>' succeed",
                            "Performing of 'Check number 5 by criteria \n" +
                                    "Sqr value\n" +
                                    "           is <25.0>' succeed",
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
                    check -> {
                        check.assertThat("Check number 4", 4,
                                shouldMatch("Is integer",
                                        (Function<Integer, Boolean>) number ->
                                                Integer.class.isAssignableFrom(number.getClass()),
                                        is(true))

                                        .and("Sqrt value",
                                                number -> sqrt(number.doubleValue()),
                                                is(2D)));

                        check.assertThat("Check number 4",
                                4,
                                is(4));

                        check.assertThat("Check number 5", 5,
                                shouldMatch("Sqr value",
                                        integer -> pow(integer, 2),
                                        is(25D)));
                    }));

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
                    check -> {
                        check.assertThat("Check number 4", 4,
                                shouldMatch("Is integer",
                                        (Function<Integer, Boolean>) number ->
                                                Integer.class.isAssignableFrom(number.getClass()),
                                        is(true))

                                        .and("Sqrt value",
                                                number -> sqrt(number.doubleValue()),
                                                is(2D)));

                        check.assertThat("Check number 4",
                                4,
                                is(4));

                        check.assertThat("Check number 5", 5,
                                shouldMatch("Sqr value",
                                        integer -> pow(integer, 2),
                                        is(25D)));
                    }));

            assertThat("Logged messages",
                    DefaultListLogger.messages,
                    contains("Performing of 'Check number 4 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' succeed",
                            "Performing of 'Check number 4 by criteria is <4>' succeed",
                            "Performing of 'Check number 5 by criteria \n" +
                                    "Sqr value\n" +
                                    "           is <25.0>' succeed",
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
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsNotDefined() {
        try {
            check.assertThat("Check number 9", 9,
                    shouldMatch("Is integer",
                            (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                            .and("Sqrt value",
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
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.assertThat("Check number 9", 9,
                    shouldMatch("Is integer",
                            (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                            .and("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
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
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfLinearNegativeCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.assertThat("Check number 9", 9,
                    shouldMatch("Is integer",
                            (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                            .and("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Mismatched object 9",
                            "'Check number 9 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' failed"));
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
            check.assertThat("Check number 9", 9,
                    shouldMatch("Is integer",
                            (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true))

                            .and("Sqrt value",
                                    number -> sqrt(number.doubleValue()),
                                    is(2D)));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Mismatched object 9",
                            "'Check number 9 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' failed"));
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
                    check.assertThat("Check number 9",
                            9,
                            shouldMatch("Is integer",
                                    (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                                    .and("Sqrt value",
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
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsSuccess() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.assertThat("Check number 9",
                            9,
                            shouldMatch("Is integer",
                                    (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                                    .and("Sqrt value",
                                            number -> sqrt(number.doubleValue()),
                                            is(2D)))));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
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
                    "             It was expected that Sqrt value suits criteria 'is <2.0>'. Actual result: was <3.0>")
    public void testOfPerformNegativeCaseWhenEventIsFailure() {
        DO_CAPTURES_OF_INSTANCE.accept(FAILURE.name());
        try {
            check.perform(action("Check number 9", check ->
                    check.assertThat("Check number 9",
                            9,
                            shouldMatch("Is integer",
                                    (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                                    .and("Sqrt value",
                                            number -> sqrt(number.doubleValue()),
                                            is(2D)))));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Mismatched object 9",
                            "'Check number 9 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' failed",
                            "'Check number 9' failed"));
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
                    check.assertThat("Check number 9",
                            9,
                            shouldMatch("Is integer",
                                    (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                                    is(true))

                                    .and("Sqrt value",
                                            number -> sqrt(number.doubleValue()),
                                            is(2D)))));
        }
        finally {
            getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
            assertThat(DefaultListLogger.messages,
                    contains("Mismatched object 9",
                            "'Check number 9 by criteria \n" +
                                    "Is integer\n" +
                                    "           is <true>\n" +
                                    "Sqrt value\n" +
                                    "           is <2.0>' failed",
                            "'Check number 9' failed"));
        }
    }
}
