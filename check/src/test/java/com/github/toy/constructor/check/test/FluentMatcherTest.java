package com.github.toy.constructor.check.test;

import com.github.toy.constructor.check.hamcrest.FluentMatcher;
import org.testng.annotations.Test;

import static com.github.toy.constructor.check.hamcrest.FluentMatcher.shouldMatch;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class FluentMatcherTest {

    private static final FluentMatcher<Double> MATCHER_UNDER_TEST =
            shouldMatch(greaterThan(0D))
                    .and(greaterThan(5D), lessThan(100D))
                    .and(toGet("Floating part of the number", doubleValue -> doubleValue - doubleValue.intValue()),
                            is(0D))
                    .and(toGet("Sqrt value", StrictMath::sqrt), greaterThan(2D));

    @Test
    public void stringDescriptionTest() {
        assertThat("String description of the fluent matcher", MATCHER_UNDER_TEST.toString(), is(
                "\n- suits criteria:\n" +
                "             a value greater than <0.0>\n" +
                "- suits criteria:\n" +
                "             a value greater than <5.0>\n" +
                "             a value less than <100.0>\n" +
                "- Floating part of the number suits criteria:\n" +
                "             is <0.0>\n" +
                "- Sqrt value suits criteria:\n" +
                "             a value greater than <2.0>\n"));
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "Try to check number 4.4\n" +
                    "Expected:  \n" +
                    "- suits criteria:\n" +
                    "             a value greater than <0.0>\n" +
                    "- suits criteria:\n" +
                    "             a value greater than <5.0>\n" +
                    "             a value less than <100.0>\n" +
                    "- Floating part of the number suits criteria:\n" +
                    "             is <0.0>\n" +
                    "- Sqrt value suits criteria:\n" +
                    "             a value greater than <2.0>\n" +
                    "\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches \n" +
                    "\n" +
                    "- expected suits criteria:\n" +
                    "             a value greater than <5.0>\n" +
                    "             a value less than <100.0>.\n" +
                    "  expected a value greater than <5.0>. Actual result is <4.4> was less than <5.0>\n" +
                    "\n" +
                    "- expected Floating part of the number suits criteria:\n" +
                    "             is <0.0>.\n" +
                    "  expected is <0.0>. Actual result is was <0.40000000000000036>")
    public void assertErrorMessageOnMismatchTest() {
        assertThat("Try to check number 4.4", 4.4D, MATCHER_UNDER_TEST);
    }
}
