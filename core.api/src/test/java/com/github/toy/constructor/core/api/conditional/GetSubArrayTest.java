package com.github.toy.constructor.core.api.conditional;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.ToGetSubArray.getArray;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetSubArrayTest extends BaseConditionalTest {

    private static final String ARRAY_CONVERTED_FROM_LIST = "Array converted from list";

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).toString(),
                is(format("%s. Criteria: %s", ARRAY_CONVERTED_FROM_LIST, "equals " + A_UPPER + " ignore case")));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput2() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput3() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput4() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput5() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput6() {
        assertThat( getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput7() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput8() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput9() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput10() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput11() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException2() {
        assertThat( getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException3() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, false).apply(LITERAL_LIST),
                emptyArray());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException2() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, false).apply(LITERAL_LIST),
                emptyArray());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException3() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, false).apply(LITERAL_LIST),
                emptyArray());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult2() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult3() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals W ignore case", VALUE_W), true, true)
                        .apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult4() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult5() {
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS).apply(EMPTY_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS,
                true,
                true).apply(LITERAL_LIST),
                emptyArray());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                condition("equals W ignore case", VALUE_W),
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS,
                true,
                true).apply(LITERAL_LIST),
                emptyArray());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOut2() {
        long start = System.currentTimeMillis();
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS).apply(EMPTY_LIST),
                emptyArray());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckSleeping2() {
        long start = System.currentTimeMillis();
        assertThat(getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyArray());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutAndException() {
        long start = System.currentTimeMillis();
        try {
            getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                    condition("equals W ignore case", VALUE_W),
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
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                    condition("equals W ignore case", VALUE_W),
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
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
                    condition("equals W ignore case", VALUE_W),
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
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getArray(ARRAY_CONVERTED_FROM_LIST, CONVERT_LIST_TO_ARRAY,
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
