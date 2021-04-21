package ru.tinkoff.qa.neptune.check.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.evaluateAndCheck;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
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


    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = ".*['List of mismatches:']" +
            "*['Sqrt value']" +
            "*['Expected: is <2.0>']" +
            "*['but: was <3.0>']")
    public void test2() {
        try {
            check(9,
                    match("Is integer",
                            number -> Integer.class.isAssignableFrom(number.getClass()),
                            is(true)),
                    match("Sqrt value",
                            number -> sqrt(number.doubleValue()),
                            is(2D)));
        } finally {
            assertThat(MESSAGES, contains(EXPECTED_LOGGER_MESSAGES2.toArray()));
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
}
