package com.github.toy.constructor.core.api.test.conditional;

import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetObjectFromArray.getFromArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetFromArrayTest extends BaseConditionalTest {

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value", null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput2() {
        getFromArray("Value", null, VALUE_A, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput3() {
        getFromArray("Value", null, VALUE_A, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput4() {
        getFromArray("Value", null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput5() {
        getFromArray("Value", null, VALUE_A, FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput6() {
        getFromArray("Value", null, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput7() {
        getFromArray("Value", null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput8() {
        getFromArray("Value", null, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput9() {
        getFromArray("Value", null, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput10() {
        getFromArray("Value", null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput11() {
        getFromArray("Value", null, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput12() {
        getFromArray("Value", null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput2() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput3() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput4() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput5() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput6() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput7() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput8() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput9() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY,  () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput10() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput11() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput12() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY);
        fail("The exception throwing was expected");
    }


    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput2() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput3() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput4() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput5() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null, FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput6() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput2() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput3() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput4() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput5() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A, FIVE_SECONDS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput6() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Array converted from list) with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput2() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true, () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Array converted from list) with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput3() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Array converted from list) with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput4() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).toString(),
                is("Value from (Array converted from list) with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput5() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).toString(),
                is("Value from (Array converted from list) with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput6() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).toString(),
                is("Value from (Array converted from list) with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput7() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Array converted from list). Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput8() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), FIVE_SECONDS, () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Array converted from list). Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput9() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Array converted from list)"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput10() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), FIVE_SECONDS, FIVE_HUNDRED_MILLIS).toString(),
                is("Value from (Array converted from list). Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput11() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), FIVE_SECONDS).toString(),
                is("Value from (Array converted from list). Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput12() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY)).toString(),
                is("Value from (Array converted from list)"));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput2() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput3() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput4() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput5() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput6() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput7() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput8() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput9() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput10() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput11() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput12() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY))
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputIgnoringException() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputIgnoringException2() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS,
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputIgnoringException3() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithoutIgnoringException() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithoutIgnoringException2() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS,
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithoutIgnoringException3() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult2() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_A),
                FIVE_SECONDS,
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult3() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_A),
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult4() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult5() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult6() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY)).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_W),
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_W),
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOut2() {
        long start = System.currentTimeMillis();
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckSleeping2() {
        long start = System.currentTimeMillis();
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOutAndException() {
        long start = System.currentTimeMillis();
        try {
            getFromArray("Value",
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromArray("Value",
                    toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getFromArray("Value",
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromArray("Value",
                    toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
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
