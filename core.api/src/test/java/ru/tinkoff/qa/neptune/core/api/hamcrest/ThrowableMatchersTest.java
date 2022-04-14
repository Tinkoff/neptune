package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.arrayIncludesInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.text.StringContainsWithSeparator.withSeparator;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableIsCausedByMatcher.hasPrimaryCause;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableIsCausedByMatcher.hasRootCause;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;

public class ThrowableMatchersTest {

    private static final Throwable TEST_EXCEPTION_WITHOUT_CAUSE = new RuntimeException("Test RuntimeException Exception");
    private static final Throwable TEST_EXCEPTION_WITH_CAUSE = new IllegalStateException("Test IllegalStateException Exception",
            new IOException("Test IO Exception",
                    new IllegalArgumentException("Test Illegal Argument Exception",
                            new AssertionError("Test Assertion Error"))));

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {hasPrimaryCause(RuntimeException.class), "primary cause matches 'is object of class 'class java.lang.RuntimeException''"},
                {hasRootCause(RuntimeException.class), "has root cause that matches 'is object of class 'class java.lang.RuntimeException''"},
                {hasPrimaryCause(RuntimeException.class, "Some text"),
                        "primary cause matches 'is object of class 'class java.lang.RuntimeException', throwable has message '\"Some text\"''"},
                {hasRootCause(RuntimeException.class, "Some text"),
                        "has root cause that matches 'is object of class 'class java.lang.RuntimeException', throwable has message '\"Some text\"''"},
                {hasPrimaryCause(RuntimeException.class, containsString("Some"), containsString("text")),
                        "primary cause matches 'is object of class 'class java.lang.RuntimeException', throwable has message 'a string containing \"Some\", a string containing \"text\"''"},
                {hasRootCause(RuntimeException.class, containsString("Some"), containsString("text")),
                        "has root cause that matches 'is object of class 'class java.lang.RuntimeException', throwable has message 'a string containing \"Some\", a string containing \"text\"''"}
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {TEST_EXCEPTION_WITHOUT_CAUSE, throwableHasMessage("Test RuntimeException Exception")},
                {TEST_EXCEPTION_WITHOUT_CAUSE, throwableHasMessage(
                        startsWith("Test"),
                        withSeparator(" ", arrayIncludesInOrder("RuntimeException", "Exception")))},
                {TEST_EXCEPTION_WITH_CAUSE, hasPrimaryCause(IOException.class)},
                {TEST_EXCEPTION_WITH_CAUSE, hasPrimaryCause(IOException.class, "Test IO Exception")},
                {TEST_EXCEPTION_WITH_CAUSE, hasPrimaryCause(IOException.class, containsString("Test"), containsString("IO"), containsString("Exception"))},

                {TEST_EXCEPTION_WITH_CAUSE, hasRootCause(AssertionError.class)},
                {TEST_EXCEPTION_WITH_CAUSE, hasRootCause(AssertionError.class, "Test Assertion Error")},
                {TEST_EXCEPTION_WITH_CAUSE, hasRootCause(AssertionError.class, containsString("Test"), containsString("Assertion"), containsString("Error"))},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {TEST_EXCEPTION_WITHOUT_CAUSE, throwableHasMessage("Test IllegalStateException Exception"),
                        "\n" +
                                "Expected: throwable has message '\"Test IllegalStateException Exception\"'\n" +
                                "     but: was \"Test RuntimeException Exception\""},
                {TEST_EXCEPTION_WITHOUT_CAUSE, throwableHasMessage(
                        startsWith("Test"),
                        withSeparator(" ", arrayIncludesInOrder("IllegalStateException", "Exception"))),
                        "\n" +
                                "Expected: throwable has message 'a string starting with \"Test\", string has substring(s) separated by <' '>: includes in following order: \"IllegalStateException\", \"Exception\"'\n" +
                                "     but: Not present item: \"IllegalStateException\""
                },
                {TEST_EXCEPTION_WITHOUT_CAUSE,
                        hasRootCause(IOException.class),
                        "\n" +
                                "Expected: has root cause that matches 'is object of class 'class java.io.IOException''\n" +
                                "     but: Null value. All checks were stopped"},
                {TEST_EXCEPTION_WITHOUT_CAUSE,
                        hasPrimaryCause(AssertionError.class),
                        "\n" +
                                "Expected: primary cause matches 'is object of class 'class java.lang.AssertionError''\n" +
                                "     but: Null value. All checks were stopped"},

                {TEST_EXCEPTION_WITHOUT_CAUSE,
                        hasRootCause(AssertionError.class),
                        "\n" +
                                "Expected: has root cause that matches 'is object of class 'class java.lang.AssertionError''\n" +
                                "     but: Null value. All checks were stopped"},

                {TEST_EXCEPTION_WITH_CAUSE,
                        hasPrimaryCause(AssertionError.class, "Test IO Exception"),
                        "\n" +
                                "Expected: primary cause matches 'is object of class 'class java.lang.AssertionError', throwable has message '\"Test IO Exception\"''\n" +
                                "     but: Class of object was <class java.io.IOException>"},

                {TEST_EXCEPTION_WITH_CAUSE,
                        hasPrimaryCause(IOException.class, "Test Assertion Error"),
                        "\n" +
                                "Expected: primary cause matches 'is object of class 'class java.io.IOException', throwable has message '\"Test Assertion Error\"''\n" +
                                "     but: was \"Test IO Exception\""},

                {TEST_EXCEPTION_WITH_CAUSE,
                        hasPrimaryCause(IOException.class, containsString("Test"), containsString("Assertion"), containsString("Exception")),
                        "\n" +
                                "Expected: primary cause matches 'is object of class 'class java.io.IOException', throwable has message 'a string containing \"Test\", a string containing \"Assertion\", a string containing \"Exception\"''\n" +
                                "     but: was \"Test IO Exception\""
                },

                {TEST_EXCEPTION_WITH_CAUSE,
                        hasPrimaryCause(AssertionError.class, "Test IO Exception"),
                        "\n" +
                                "Expected: primary cause matches 'is object of class 'class java.lang.AssertionError', throwable has message '\"Test IO Exception\"''\n" +
                                "     but: Class of object was <class java.io.IOException>"},

                {TEST_EXCEPTION_WITH_CAUSE,
                        hasRootCause(OutOfMemoryError.class),
                        "\n" +
                                "Expected: has root cause that matches 'is object of class 'class java.lang.OutOfMemoryError''\n" +
                                "     but: Not present cause of the throwable: is object of class 'class java.lang.OutOfMemoryError'"},

                {TEST_EXCEPTION_WITH_CAUSE,
                        hasRootCause(IOException.class, "Test Assertion Error"),
                        "\n" +
                                "Expected: has root cause that matches 'is object of class 'class java.io.IOException', throwable has message '\"Test Assertion Error\"''\n" +
                                "     but: Not present cause of the throwable: is object of class 'class java.io.IOException', throwable has message '\"Test Assertion Error\"'"},

                {TEST_EXCEPTION_WITH_CAUSE,
                        hasRootCause(IOException.class, containsString("Test"), containsString("Assertion"), containsString("Exception")),
                        "\n" +
                                "Expected: has root cause that matches 'is object of class 'class java.io.IOException', throwable has message 'a string containing \"Test\", a string containing \"Assertion\", a string containing \"Exception\"''\n" +
                                "     but: Not present cause of the throwable: is object of class 'class java.io.IOException', throwable has message 'a string containing \"Test\", a string containing \"Assertion\", a string containing \"Exception\"'"
                },
        };
    }

    @Test(dataProvider = "data")
    public void testOfDescription(Matcher<?> matcher, String expected) {
        assertThat(matcher.toString(), is(expected));
    }

    @Test(dataProvider = "data2")
    public void testWithoutAssertError(Throwable toCheck, Matcher<? super Throwable> matcher) {
        assertThat(toCheck, matcher);
    }

    @Test(dataProvider = "data3")
    public void testWithAssertError(Throwable toCheck, Matcher<? super Throwable> matcher, String expectedErrorMessage) {
        try {
            assertThat(toCheck, matcher);
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(expectedErrorMessage));
            return;
        }
        fail("Exception was expected");
    }
}
