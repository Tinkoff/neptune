package com.github.toy.constructor.check.test;

import com.github.toy.constructor.check.Check;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.function.Function;

import static com.github.toy.constructor.check.hamcrest.FluentMatcher.shouldMatch;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.Substitution.getSubstituted;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.List.of;
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
    public void testOfLinearPositiveCase() {
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

        MatcherAssert.assertThat("Logged messages",
                DefaultListLogger.messages,
                contains("Getting of 'Is integer' succeed",
                        "Getting of 'Sqrt value' succeed",
                        "Performing of 'Check number 4 by criteria \n" +
                                "Is integer\n" +
                                "           is <true>\n" +
                                "Sqrt value\n" +
                                "           is <2.0>' succeed",
                        "Performing of 'Check number 4 by criteria is <4>' succeed",
                        "Getting of 'Sqr value' succeed",
                        "Performing of 'Check number 5 by criteria \n" +
                                "Sqr value\n" +
                                "           is <25.0>' succeed"));
    }

    @Test
    public void testOfPositivePerformCase() {
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

        MatcherAssert.assertThat("Logged messages",
                DefaultListLogger.messages,
                contains("Getting of 'Is integer' succeed",
                        "Getting of 'Sqrt value' succeed",
                        "Performing of 'Check number 4 by criteria \n" +
                                "Is integer\n" +
                                "           is <true>\n" +
                                "Sqrt value\n" +
                                "           is <2.0>' succeed",
                        "Performing of 'Check number 4 by criteria is <4>' succeed",
                        "Getting of 'Sqr value' succeed",
                        "Performing of 'Check number 5 by criteria \n" +
                                "Sqr value\n" +
                                "           is <25.0>' succeed",
                        "Performing of 'Check numbers 4 and 5' succeed"));
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
    public void testOfLinearAssertionCase() {
        check.assertThat("Check number 9", 9,
                shouldMatch("Is integer",
                        (Function<Integer, Boolean>) number -> Integer.class.isAssignableFrom(number.getClass()),
                        is(true))

                        .and("Sqrt value",
                                number -> sqrt(number.doubleValue()),
                                is(2D)));
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
    public void testOfPerformAssertionCase() {
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
}
