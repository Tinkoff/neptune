package ru.tinkoff.qa.neptune.check.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.evaluateAndCheck;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.MatchAction.matchOr;
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
        }
    }

    @Test
    public void test3() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
        check("Given value", (Object) null, match(nullValue()));

        assertThat(MESSAGES,
                contains("Check: Given value has started",
                        "Assert: null has started",
                        "Event finished",
                        "Event finished"));
    }

    @Test
    public void test4() {
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
    public void test5() {
        check("Tested number",
                9,
                matchOr(greaterThan(0),
                        lessThan(10),
                        greaterThan(5)),
                matchOr("Sqrt", (Function<Integer, Double>) Math::sqrt,
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
    public void test6() {
        try {
            check("Tested number",
                    4,
                    matchOr(greaterThan(5),
                            lessThan(-10),
                            instanceOf(Float.class)),
                    matchOr("Sqrt", (Function<Integer, Double>) Math::sqrt,
                            is(3D),
                            lessThan(0D),
                            lessThan(2D)));
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is("Found mismatches:\r\n" +
                    "Expected: a value greater than <5> or a value less than <-10> or an instance of java.lang.Float\r\n" +
                    "Checked value: '4'\r\n" +
                    "Result: Does not match any of the listed criteria" +
                    "\r\n" +
                    "\r\n" +
                    "Expected: 'Sqrt' is <3.0> or a value less than <0.0> or a value less than <2.0>\r\n" +
                    "Checked value: '2.0'\r\n" +
                    "Result: Does not match any of the listed criteria"));
        }

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
    }
}
