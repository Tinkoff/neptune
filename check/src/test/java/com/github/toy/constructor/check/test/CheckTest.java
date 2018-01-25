package com.github.toy.constructor.check.test;

import com.github.toy.constructor.check.Check;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.function.Function;

import static com.github.toy.constructor.check.hamcrest.FluentMatcher.shouldMatch;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.hamcrest.Matchers.*;

public class CheckTest {

    private Check check;

    @BeforeTest
    public void beforeTest() throws Exception {
        check = getSubstituted(Check.class, params());
    }

    @Test
    public void testOfLinearPositiveCase() {
        check.check("Check number 4",
                4,
                shouldMatch(toGet("Is integer",
                        (Function<Integer, Boolean>) number ->
                                Integer.class.isAssignableFrom(number.getClass())), is(true))
                        .and(toGet("Sqrt value",
                                number -> sqrt(number.doubleValue())), is(2D)),
                is(4),
                allOf(greaterThan(0), lessThanOrEqualTo(100)))

                .check("Check number 5", 5,
                        is(5),
                        shouldMatch(toGet("Sqr values",
                                (Function<Integer, Double>) integer -> pow(integer, 2)), is(25D)));
    }

    @Test
    public void testOfPositivePerformCase() {
        check.perform(action("Check numbers 4 and 5",
                check -> {
                    check.check("Check number 4",
                            4,
                            shouldMatch(toGet("Is integer",
                                    (Function<Integer, Boolean>) number ->
                                            Integer.class.isAssignableFrom(number.getClass())), is(true))
                                    .and(toGet("Sqrt value",
                                            number -> sqrt(number.doubleValue())), is(2D)),
                            is(4),
                            allOf(greaterThan(0), lessThanOrEqualTo(100)));

                    check.check("Check number 5",
                            5,
                            is(5),
                            shouldMatch(toGet("Sqr values",
                                    (Function<Integer, Double>) integer -> pow(integer, 2)), is(25D)));
                }));
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "Failed assertions\n" +
                    "==================================\n" +
                    "\n" +
                    "Expected:  \n" +
                    "- Is integer suits criteria:\n" +
                    "             is <true>\n" +
                    "- Sqrt value suits criteria:\n" +
                    "             is <2.0>\n" +
                    "\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches \n" +
                    "\n" +
                    "- expected Sqrt value suits criteria:\n" +
                    "             is <2.0>.\n" +
                    "  expected is <2.0>. Actual result is was <3.0>\n" +
                    "==================================\n" +
                    "\n" +
                    "Expected: is <4>\n" +
                    "     but: was <9>")
    public void testOfLinearAssertionCase() {
        check.check("Check number 9",
                9,
                shouldMatch(toGet("Is integer",
                        (Function<Integer, Boolean>) number ->
                                Integer.class.isAssignableFrom(number.getClass())), is(true))
                        .and(toGet("Sqrt value",
                                number -> sqrt(number.doubleValue())), is(2D)),
                is(4), allOf(greaterThan(0), lessThanOrEqualTo(100)))

                .check("Check number 6", 6,
                        is(5),
                        shouldMatch(toGet("Sqr values", (Function<Integer, Double>) integer -> pow(integer, 2)),
                                is(25D)));
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "Failed assertions\n" +
                    "==================================\n" +
                    "\n" +
                    "Expected:  \n" +
                    "- Is integer suits criteria:\n" +
                    "             is <true>\n" +
                    "- Sqrt value suits criteria:\n" +
                    "             is <2.0>\n" +
                    "\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches \n" +
                    "\n" +
                    "- expected Sqrt value suits criteria:\n" +
                    "             is <2.0>.\n" +
                    "  expected is <2.0>. Actual result is was <3.0>\n" +
                    "==================================\n" +
                    "\n" +
                    "Expected: is <4>\n" +
                    "     but: was <9>")
    public void testOfPerformAssertionCase() {
        check.perform(action("Check number 9", check ->
                check.check("Check number 9 and 6",
                        9,
                        shouldMatch(toGet("Is integer",
                                (Function<Integer, Boolean>) number ->
                                        Integer.class.isAssignableFrom(number.getClass())), is(true))
                                .and(toGet("Sqrt value",
                                        number -> sqrt(number.doubleValue())), is(2D)),
                        is(4),
                        allOf(greaterThan(0), lessThanOrEqualTo(100)))));
    }
}
