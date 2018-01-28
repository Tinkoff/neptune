package com.github.toy.constructor.check.test;

import com.github.toy.constructor.check.hamcrest.FluentMatcher;
import org.testng.annotations.Test;

import static com.github.toy.constructor.check.hamcrest.FluentMatcher.shouldMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class FluentMatcherTest {

    private static final FluentMatcher<Double> MATCHER_UNDER_TEST =
            shouldMatch(greaterThan(0D), greaterThan(5D), lessThan(100D))
                    .and("Floating part of the number", doubleValue -> doubleValue - doubleValue.intValue(),
                            is(0D))
                    .and("Sqrt value", StrictMath::sqrt, greaterThan(2D));

    @Test
    public void stringDescriptionTest() {
        assertThat("String description of the fluent matcher", MATCHER_UNDER_TEST.toString(), is(
                "\n" +
                        "inspected value\n" +
                        "           a value greater than <0.0>\n" +
                        "           a value greater than <5.0>\n" +
                        "           a value less than <100.0>\n" +
                        "Floating part of the number\n" +
                        "           is <0.0>\n" +
                        "Sqrt value\n" +
                        "           a value greater than <2.0>"));
    }

    @Test(expectedExceptions = AssertionError.class,
            expectedExceptionsMessageRegExp = "Try to check number 4.4\n" +
                    "Expected:  \n" +
                    "inspected value\n" +
                    "           a value greater than <0.0>\n" +
                    "           a value greater than <5.0>\n" +
                    "           a value less than <100.0>\n" +
                    "Floating part of the number\n" +
                    "           is <0.0>\n" +
                    "Sqrt value\n" +
                    "           a value greater than <2.0>\n" +
                    "     but: \n" +
                    "\n" +
                    "Detected mismatches:\n" +
                    "===================================\n" +
                    "inspected value:\n" +
                    "             It was expected that inspected value suits criteria 'a value greater than <5.0>'. Actual result: <4.4> was less than <5.0>\n" +
                    "Floating part of the number:\n" +
                    "             It was expected that Floating part of the number suits criteria 'is <0.0>'. Actual result: was <0.40000000000000036>")
    public void assertErrorMessageOnMismatchTest() {
        assertThat("Try to check number 4.4", 4.4D, MATCHER_UNDER_TEST);
    }
}
