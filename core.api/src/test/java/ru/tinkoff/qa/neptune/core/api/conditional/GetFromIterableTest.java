package ru.tinkoff.qa.neptune.core.api.conditional;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.StoryWriter;

import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromIterable.getFromIterable;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.testng.Assert.fail;

public class GetFromIterableTest extends BaseConditionalTest {
    private static final String SET_CONVERTED_FROM_LIST = "Set converted from list";

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND).toString(),
                is(format("%s [Criteria: %s", SET_CONVERTED_FROM_LIST, "equals " + A_UPPER + " ignore case]")));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput2() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput3() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput4() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true)
                        .apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput5() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true)
                        .apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput6() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true)
                        .apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput7() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput8() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput9() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput10() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput11() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput12() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException2() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS,
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException3() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithoutIgnoringException() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithoutIgnoringException2() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS,
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithoutIgnoringException3() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals W ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult2() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals W ignore case", VALUE_A),
                FIVE_SECONDS,
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult3() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals W ignore case", VALUE_A),
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult4() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult5() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult6() {
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS,
                true,
                true).apply(LITERAL_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                StoryWriter.condition("equals W ignore case", VALUE_W),
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS,
                true,
                true).apply(LITERAL_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOut2() {
        long start = System.currentTimeMillis();
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckSleeping2() {
        long start = System.currentTimeMillis();
        assertThat(getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutAndException() {
        long start = System.currentTimeMillis();
        try {
            getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                    StoryWriter.condition("equals W ignore case", VALUE_W),
                    FIVE_SECONDS,
                    true,
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                    StoryWriter.condition("equals W ignore case", VALUE_W),
                    FIVE_SECONDS,
                    FIVE_HUNDRED_MILLIS,
                    true,
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
