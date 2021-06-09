package ru.tinkoff.qa.neptune.check.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.evaluateAndCheck;
import static ru.tinkoff.qa.neptune.check.MatchAction.*;
import static ru.tinkoff.qa.neptune.check.test.TestEventLogger.MESSAGES;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

public class CheckTest {

    @BeforeMethod
    public void beforeMethod() {
        MESSAGES.clear();
    }

    private static final List<String> EXPECTED_LOGGER_MESSAGES1 =
            of("Check: 4 has started",
                    "Assert: Is integer is <true> has started",
                    "Event finished",
                    "Assert: Sqrt value is <2.0> has started",
                    "Event finished",
                    "Event finished",
                    "Check: 5 has started",
                    "Assert: Sqr value is <25.0> has started",
                    "Event finished",
                    "Event finished");

    private static final List<String> EXPECTED_LOGGER_MESSAGES2 =
            of("Check: 9 has started",
                    "Assert: Is integer is <true> has started",
                    "Event finished",
                    "Assert: Sqrt value is <2.0> has started",
                    "java.lang.AssertionError has been thrown",
                    "Event finished",
                    "java.lang.AssertionError has been thrown",
                    "Event finished");

    @Test
    public void test1() {
        check(4, match("Is integer", number ->
                        Integer.class.isAssignableFrom(number.getClass()),
                is(true)),
                match("Sqrt value", number -> sqrt(number.doubleValue()), is(2D)));

        check(5, match("Sqr value",
                number -> pow(number.doubleValue(), 2),
                is(25D)));

        assertThat(MESSAGES,
                contains(EXPECTED_LOGGER_MESSAGES1.toArray()));
    }


    @Test
    public void test2() {
        try {
            check(9,
                    match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        } catch (AssertionError e) {
            assertThat(MESSAGES, contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
            assertThat(e.getMessage(), is("Found mismatches:\r\n" +
                    "Expected: 'Sqrt value' is <2.0>\r\n" +
                    "Checked value: '3.0'\r\n" +
                    "Result: was <3.0>"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test3() {
        var benchMarkStart = currentTimeMillis();
        check(4,
                match("Sqrt value",
                        number -> sqrt(number.doubleValue()),
                        ofSeconds(3),
                        is(2D)));

        var benchMarkEnd = currentTimeMillis();
        assertThat(benchMarkEnd - benchMarkStart, lessThanOrEqualTo(800L));
    }

    @Test
    public void test4() {
        var benchMarkStart = currentTimeMillis();
        try {
            check(9,
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            ofSeconds(3),
                            is(2D)));
        } catch (AssertionError e) {
            var benchMarkEnd = currentTimeMillis();
            assertThat(benchMarkEnd - benchMarkStart, lessThan(4000L));
            assertThat(benchMarkEnd - benchMarkStart, greaterThanOrEqualTo(3000L));

            assertThat(e.getMessage(), is("Found mismatches:\r\n" +
                    "Expected: 'Sqrt value' is <2.0>\r\n" +
                    "Checked value: '3.0'\r\n" +
                    "Result: was <3.0>. Time of the waiting for the matching: 00:00:03.000"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test5() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
        check("Given value", (Object) null, match(nullValue()));

        assertThat(MESSAGES,
                contains("Check: Given value has started",
                        "Assert: null has started",
                        "Event finished",
                        "Event finished"));
    }

    @Test
    public void test6() {
        evaluateAndCheck("Sqrt value of 9",
                () -> sqrt(9),
                match(is(3D)));

        assertThat(MESSAGES,
                contains("Check: Sqrt value of 9 has started",
                        "Check: Sqrt value of 9 has started",
                        "Assert: is <3.0> has started",
                        "Event finished",
                        "Event finished",
                        "Event finished"));
    }

    @Test
    public void test7() {
        check("Tested number",
                9,
                matchAny(greaterThan(0),
                        lessThan(10),
                        greaterThan(5)),
                matchAny("Sqrt", (Function<Integer, Double>) Math::sqrt,
                        is(3D),
                        greaterThan(0D),
                        lessThan(2D)));

        assertThat(MESSAGES,
                contains("Check: Tested number has started",
                        "Assert: a value greater than <0> or a value less than <10> or a value greater than <5> has started",
                        "Event finished",
                        "Assert: Sqrt is <3.0> or a value greater than <0.0> or a value less than <2.0> has started",
                        "Event finished",
                        "Event finished"));
    }

    @Test
    public void test8() {
        try {
            check("Tested number",
                    4,
                    matchAny(greaterThan(5),
                            lessThan(-10),
                            instanceOf(Float.class)),
                    matchAny("Sqrt", (Function<Integer, Double>) Math::sqrt,
                            is(3D),
                            lessThan(0D),
                            lessThan(2D)));
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is("Found mismatches:\r\n" +
                    "Expected: a value greater than <5> or a value less than <-10> or an instance of java.lang.Float\r\n" +
                    "Checked value: '4'\r\n" +
                    "Result: <4> doesn't match any of listed criteria" +
                    "\r\n" +
                    "\r\n" +
                    "Expected: 'Sqrt' is <3.0> or a value less than <0.0> or a value less than <2.0>\r\n" +
                    "Checked value: '2.0'\r\n" +
                    "Result: <2.0> doesn't match any of listed criteria"));

            assertThat(MESSAGES,
                    contains("Check: Tested number has started",
                            "Assert: a value greater than <5> or a value less than <-10> or an instance of java.lang.Float has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "Assert: Sqrt is <3.0> or a value less than <0.0> or a value less than <2.0> has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "java.lang.AssertionError has been thrown",
                            "Event finished"));

            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test9() {
        var benchMarkStart = currentTimeMillis();
        check("Tested number",
                9,
                matchAny(ofSeconds(3),
                        greaterThan(0),
                        lessThan(10),
                        greaterThan(5)));
        var benchMarkEnd = currentTimeMillis();
        assertThat(benchMarkEnd - benchMarkStart, lessThanOrEqualTo(800L));
    }

    @Test
    public void test10() {
        var benchMarkStart = currentTimeMillis();
        try {
            check("Tested number",
                    4,
                    matchAny(ofSeconds(3),
                            greaterThan(5),
                            lessThan(-10),
                            instanceOf(Float.class)));
        } catch (AssertionError e) {
            var benchMarkEnd = currentTimeMillis();
            assertThat(benchMarkEnd - benchMarkStart, lessThan(4000L));
            assertThat(benchMarkEnd - benchMarkStart, greaterThanOrEqualTo(3000L));

            assertThat(e.getMessage(), is("Found mismatches:\r\n" +
                    "Expected: a value greater than <5> or a value less than <-10> or an instance of java.lang.Float\r\n" +
                    "Checked value: '4'\r\n" +
                    "Result: <4> doesn't match any of listed criteria. Time of the waiting for the matching: 00:00:03.000"));
            return;
        }

        fail("Exception was expected");
    }


    @Test
    public void test11() {
        check("Tested number",
                9,
                matchNot(greaterThan(10)),
                matchNot("Sqrt", (Function<Integer, Double>) Math::sqrt, is(4D)));

        assertThat(MESSAGES,
                contains("Check: Tested number has started",
                        "Assert: not a value greater than <10> has started",
                        "Event finished",
                        "Assert: Sqrt not is <4.0> has started",
                        "Event finished",
                        "Event finished"));
    }

    @Test
    public void test12() {
        try {
            check("Tested number",
                    4,
                    matchNot(greaterThan(-10)),
                    matchNot("Sqrt", (Function<Integer, Double>) Math::sqrt, is(2D)));
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is("Found mismatches:\r\n" +
                    "Expected: not a value greater than <-10>\r\n" +
                    "Checked value: '4'\r\n" +
                    "Result: a value greater than <-10>\r\n" +
                    "\r\n" +
                    "Expected: 'Sqrt' not is <2.0>\r\n" +
                    "Checked value: '2.0'\r\n" +
                    "Result: is <2.0>"));

            assertThat(MESSAGES,
                    contains("Check: Tested number has started",
                            "Assert: not a value greater than <-10> has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "Assert: Sqrt not is <2.0> has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "java.lang.AssertionError has been thrown",
                            "Event finished"));

            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test13() {
        var benchMarkStart = currentTimeMillis();
        check("Tested number",
                9,
                matchNot(ofSeconds(3), greaterThan(10)));
        var benchMarkEnd = currentTimeMillis();
        assertThat(benchMarkEnd - benchMarkStart, lessThanOrEqualTo(800L));
    }

    @Test
    public void test14() {
        var benchMarkStart = currentTimeMillis();
        try {
            check("Tested number",
                    4,
                    matchNot(ofSeconds(3), greaterThan(-10)));
        } catch (AssertionError e) {
            var benchMarkEnd = currentTimeMillis();
            assertThat(benchMarkEnd - benchMarkStart, lessThan(4000L));
            assertThat(benchMarkEnd - benchMarkStart, greaterThanOrEqualTo(3000L));

            assertThat(e.getMessage(), is("Found mismatches:\r\n" +
                    "Expected: not a value greater than <-10>\r\n" +
                    "Checked value: '4'\r\n" +
                    "Result: a value greater than <-10>. Time of the waiting for the matching: 00:00:03.000"));
            return;
        }

        fail("Exception was expected");
    }


    @Test
    public void test15() {
        check("Tested number",
                9,
                matchOnlyOne(lessThan(0),
                        greaterThan(10),
                        greaterThan(5)),
                matchOnlyOne("Sqrt", (Function<Integer, Double>) Math::sqrt,
                        is(3D),
                        greaterThan(5D),
                        lessThan(2D)));

        assertThat(MESSAGES,
                contains("Check: Tested number has started",
                        "Assert: a value less than <0> xor a value greater than <10> xor a value greater than <5> has started",
                        "Event finished",
                        "Assert: Sqrt is <3.0> xor a value greater than <5.0> xor a value less than <2.0> has started",
                        "Event finished",
                        "Event finished"));
    }

    @Test
    public void test16() {
        try {
            check("Tested number",
                    4,
                    matchOnlyOne(greaterThan(5),
                            lessThan(-10),
                            instanceOf(Float.class)),
                    matchOnlyOne("Sqrt", (Function<Integer, Double>) Math::sqrt,
                            is(3D),
                            lessThan(0D),
                            lessThan(2D)));
        } catch (AssertionError e) {
            assertThat(e.getMessage(), allOf(containsString("Found mismatches:"),
                    containsString("Expected: a value greater than <5> xor a value less than <-10> xor an instance of java.lang.Float"),
                    containsString("Checked value: '4'"),
                    containsString("Result: <4> doesn't match any of listed criteria"),
                    containsString("Expected: 'Sqrt' is <3.0> xor a value less than <0.0> xor a value less than <2.0>"),
                    containsString("Checked value: '2.0'"),
                    containsString("Result: <2.0> doesn't match any of listed criteria")));

            assertThat(MESSAGES,
                    contains("Check: Tested number has started",
                            "Assert: a value greater than <5> xor a value less than <-10> xor an instance of java.lang.Float has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "Assert: Sqrt is <3.0> xor a value less than <0.0> xor a value less than <2.0> has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "java.lang.AssertionError has been thrown",
                            "Event finished"));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test17() {
        try {
            check("Tested number",
                    4,
                    matchOnlyOne(greaterThan(0),
                            lessThan(-10),
                            instanceOf(Integer.class)),
                    matchOnlyOne("Sqrt", (Function<Integer, Double>) Math::sqrt,
                            is(2D),
                            greaterThan(0D),
                            lessThan(2D)));
        } catch (AssertionError e) {
            assertThat(e.getMessage(), allOf(containsString("Found mismatches:"),
                    containsString("Expected: a value greater than <0> xor a value less than <-10> xor an instance of java.lang.Integer"),
                    containsString("Checked value: '4'"),
                    containsString("Result: Value: 4. Only one of listed criteria was expected to be matched. Checks of following criteria were positive:"),
                    containsString(" a value greater than <0>"),
                    containsString("an instance of java.lang.Integer"),
                    containsString("Expected: 'Sqrt' is <2.0> xor a value greater than <0.0> xor a value less than <2.0>"),
                    containsString("Checked value: '2.0'"),
                    containsString("Result: Value: 2.0. Only one of listed criteria was expected to be matched. Checks of following criteria were positive:"),
                    containsString("is <2.0>"),
                    containsString("a value greater than <0.0>")));

            assertThat(MESSAGES,
                    contains("Check: Tested number has started",
                            "Assert: a value greater than <0> xor a value less than <-10> xor an instance of java.lang.Integer has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "Assert: Sqrt is <2.0> xor a value greater than <0.0> xor a value less than <2.0> has started",
                            "java.lang.AssertionError has been thrown",
                            "Event finished",
                            "java.lang.AssertionError has been thrown",
                            "Event finished"));

            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test18() {
        var benchMarkStart = currentTimeMillis();
        check("Tested number",
                9,
                matchOnlyOne(ofSeconds(3),
                        greaterThan(0),
                        lessThan(8),
                        greaterThan(10)));
        var benchMarkEnd = currentTimeMillis();
        assertThat(benchMarkEnd - benchMarkStart, lessThanOrEqualTo(800L));
    }

    @Test
    public void test19() {
        var benchMarkStart = currentTimeMillis();
        try {
            check("Tested number",
                    4,
                    matchOnlyOne(ofSeconds(3),
                            greaterThan(5),
                            lessThan(-10),
                            instanceOf(Float.class)));
        } catch (AssertionError e) {
            var benchMarkEnd = currentTimeMillis();
            assertThat(benchMarkEnd - benchMarkStart, lessThan(4000L));
            assertThat(benchMarkEnd - benchMarkStart, greaterThanOrEqualTo(3000L));

            assertThat(e.getMessage(), allOf(containsString("Found mismatches:"),
                    containsString("Expected: a value greater than <5> xor a value less than <-10> xor an instance of java.lang.Float"),
                    containsString("Checked value: '4'"),
                    containsString("Result: <4> doesn't match any of listed criteria. Time of the waiting for the matching: 00:00:03.000")));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void test20() {
        var benchMarkStart = currentTimeMillis();
        try {
            check("Tested number",
                    4,
                    matchOnlyOne("Sqrt",
                            (Function<Integer, Double>) Math::sqrt,
                            ofSeconds(3),
                            is(2D),
                            lessThan(-10D),
                            instanceOf(Double.class)));
        } catch (AssertionError e) {
            var benchMarkEnd = currentTimeMillis();
            assertThat(benchMarkEnd - benchMarkStart, lessThan(4000L));
            assertThat(benchMarkEnd - benchMarkStart, greaterThanOrEqualTo(3000L));

            assertThat(e.getMessage(), allOf(containsString("Found mismatches:"),
                    containsString("Expected: 'Sqrt' is <2.0> xor a value less than <-10.0> xor an instance of java.lang.Double"),
                    containsString("Checked value: '2.0'"),
                    containsString("Result: Value: 2.0. Only one of listed criteria was expected to be matched. Checks of following criteria were positive:"),
                    containsString("is <2.0>"),
                    containsString("an instance of java.lang.Double"),
                    containsString("Time of the waiting for the matching: 00:00:03.000")));
            return;
        }

        fail("Exception was expected");
    }
}
