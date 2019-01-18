package ru.tinkoff.qa.neptune.core.api.conditional;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSingleCheckedObject.getSingle;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetASingleTest extends BaseConditionalTest {

    private static final String THE_FIRST_OBJECT_DESCRIPTION = "The first object of the list";

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND).toString(),
                is(format("%s [Criteria: %s", THE_FIRST_OBJECT_DESCRIPTION, "equals " + A_UPPER + " ignore case]")));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput2() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput3() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput4() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput5() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, true)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput6() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                true).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput7() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput8() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput9() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput10() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput11() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                FIVE_SECONDS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException() {
        assertThat(getSingle("Value", GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException2() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException3() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithoutIgnoringException() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithoutIgnoringException2() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithoutIgnoringException3() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> "X",
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult2() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> "X",
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult3() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> "X",
                condition("equals W ignore case", VALUE_W), true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult4() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> null,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult5() {
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> null,
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, true).apply(LITERAL_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                condition("equals W ignore case", VALUE_W),
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS,
                true).apply(LITERAL_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOut2() {
        long start = System.currentTimeMillis();
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> null,
                FIVE_SECONDS).apply(LITERAL_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckSleeping2() {
        long start = System.currentTimeMillis();
        assertThat(getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> null,
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutAndException() {
        long start = System.currentTimeMillis();
        try {
            getSingle("Value", GET_FIRST_OBJECT_FROM_LIST,
                    condition("equals W ignore case", VALUE_W),
                    FIVE_SECONDS,
                    true,
                    () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
        }
        catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                    lessThanOrEqualTo(200L));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> null,
                    FIVE_SECONDS,
                    () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
        }
        catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                    lessThanOrEqualTo(200L));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getSingle(THE_FIRST_OBJECT_DESCRIPTION, GET_FIRST_OBJECT_FROM_LIST,
                    condition("equals W ignore case", VALUE_W),
                    FIVE_SECONDS,
                    FIVE_HUNDRED_MILLIS,
                    true,
                    () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
        }
        catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(FIVE_SECONDS.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                    lessThanOrEqualTo(600L));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getSingle(THE_FIRST_OBJECT_DESCRIPTION, s -> null,
                    FIVE_SECONDS,
                    FIVE_HUNDRED_MILLIS,
                    () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
        }
        catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(FIVE_SECONDS.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                    lessThanOrEqualTo(600L));
            throw e;
        }
        fail("The exception throwing was expected");
    }
}
