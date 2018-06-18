package com.github.toy.constructor.core.api.conditional;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetSubArray.getArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetSubArrayTest extends BaseConditionalTest {

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndArrayOutput() {
        getArray( null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput2() {
        getArray( null, VALUE_A, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput3() {
        getArray( null, VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput4() {
        getArray( null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput5() {
        getArray( null, VALUE_A, FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput6() {
        getArray( null, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput7() {
        getArray(null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput8() {
        getArray(null, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput9() {
        getArray(null, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput10() {
        getArray(null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput11() {
        getArray(null, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput() {
        getArray( CONVERT_LIST_TO_ARRAY, VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput2() {
        getArray( CONVERT_LIST_TO_ARRAY, VALUE_A,
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput3() {
        getArray( CONVERT_LIST_TO_ARRAY, VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput4() {
        getArray( CONVERT_LIST_TO_ARRAY, VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput5() {
        getArray( CONVERT_LIST_TO_ARRAY, VALUE_A,
                FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput6() {
        getArray( CONVERT_LIST_TO_ARRAY, VALUE_A,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput7() {
        getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput8() {
        getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput9() {
        getArray(CONVERT_LIST_TO_ARRAY, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput10() {
        getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput11() {
        getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput2() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput3() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput4() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput5() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput6() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                true, true);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput2() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput3() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput4() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput5() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput6() {
        getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).toString(),
                is("Array converted from list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput2() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND).toString(),
                is("Array converted from list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput3() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true,
                () -> NOTHING_WAS_FOUND).toString(),
                is("Array converted from list with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput4() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).toString(),
                is("Array converted from list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput5() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).toString(),
                is("Array converted from list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput6() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).toString(),
                is("Array converted from list with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput7() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                () -> NOTHING_WAS_FOUND).toString(),
                is("Array converted from list. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput8() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).toString(),
                is("Array converted from list. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput9() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                () -> NOTHING_WAS_FOUND).toString(),
                is("Array converted from list"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput10() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).toString(),
                is("Array converted from list. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput11() {
        assertThat("Description", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS).toString(),
                is("Array converted from list. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput2() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput3() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput4() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput5() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput6() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).apply(LITERAL_LIST),
                Matchers.arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput7() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput8() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput9() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput10() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput11() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException2() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException3() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, false).apply(LITERAL_LIST),
                emptyArray());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException2() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, false).apply(LITERAL_LIST),
                emptyArray());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException3() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, false).apply(LITERAL_LIST),
                emptyArray());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult2() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult3() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_W), true, true)
                        .apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult4() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult5() {
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS).apply(EMPTY_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat("Value", getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
        assertThat("Value",  getArray(
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
        assertThat("Value",
                getArray(toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
        assertThat("Value",
                getArray(toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
            getArray(
                    toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
            getArray(
                    toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
            getArray(
                    toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
            getArray(toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
