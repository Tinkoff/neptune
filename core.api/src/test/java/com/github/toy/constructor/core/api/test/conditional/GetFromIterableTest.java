package com.github.toy.constructor.core.api.test.conditional;

import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetObjectFromIterable.getFromIterable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.testng.Assert.fail;

public class GetFromIterableTest extends BaseConditionalTest {

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value", null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput2() {
        getFromIterable("Value", null, VALUE_A, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput3() {
        getFromIterable("Value", null, VALUE_A, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput4() {
        getFromIterable("Value", null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput5() {
        getFromIterable("Value", null, VALUE_A, FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput6() {
        getFromIterable("Value", null, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput7() {
        getFromIterable("Value", null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput8() {
        getFromIterable("Value", null, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput9() {
        getFromIterable("Value", null, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput10() {
        getFromIterable("Value", null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput11() {
        getFromIterable("Value", null, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput12() {
        getFromIterable("Value", null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput2() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput3() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput4() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput5() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput6() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput7() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput8() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput9() {
        getFromIterable("Value", CONVERT_LIST_TO_SET,  () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput10() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput11() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput12() {
        getFromIterable("Value", CONVERT_LIST_TO_SET);
        fail("The exception throwing was expected");
    }


    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput2() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput3() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput4() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput5() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput6() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput2() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput3() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput4() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput5() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A, FIVE_SECONDS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput6() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND).toString(),
                is("Value with condition equals A ignore case. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput2() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true, () -> NOTHING_WAS_FOUND).toString(),
                is("Value with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput3() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND).toString(),
                is("Value with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput4() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).toString(),
                is("Value with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput5() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).toString(),
                is("Value with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput6() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).toString(),
                is("Value with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput7() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).toString(),
                is("Value. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput8() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), FIVE_SECONDS, () -> NOTHING_WAS_FOUND).toString(),
                is("Value. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput9() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), () -> NOTHING_WAS_FOUND).toString(),
                is("Value"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput10() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), FIVE_SECONDS, FIVE_HUNDRED_MILLIS).toString(),
                is("Value. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput11() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), FIVE_SECONDS).toString(),
                is("Value. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput12() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET)).toString(),
                is("Value"));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput2() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput3() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput4() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput5() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput6() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true)
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput7() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput8() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput9() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput10() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput11() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput12() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET))
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException2() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS,
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException3() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithoutIgnoringException() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithoutIgnoringException2() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS,
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithoutIgnoringException3() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult2() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_A),
                FIVE_SECONDS,
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult3() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_A),
                true,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult4() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult5() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult6() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET)).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOut2() {
        long start = System.currentTimeMillis();
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
            getFromIterable("Value",
                    toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromIterable("Value",
                    toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
            getFromIterable("Value",
                    toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromIterable("Value",
                    toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
