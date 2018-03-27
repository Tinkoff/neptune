package com.github.toy.constructor.core.api.test.conditional;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getIterable;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getSubIterable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetSubIterableTest extends BaseConditionalTest {

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput() {
        getSubIterable("Set of values", null, VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput2() {
        getSubIterable("Set of values", null, VALUE_A,
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput3() {
        getSubIterable("Set of values", null, VALUE_A, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput4() {
        getSubIterable("Set of values", null, VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput5() {
        getSubIterable("Set of values", null, VALUE_A,
                FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput6() {
        getSubIterable("Set of values", null, VALUE_A,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput7() {
        getIterable( null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput8() {
        getIterable( null, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput9() {
        getIterable( null, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput10() {
        getIterable( null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput11() {
        getIterable( null, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput() {
        getSubIterable("Set of values", CONVERT_LIST_TO_SET, VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput2() {
        getSubIterable("Set of values", CONVERT_LIST_TO_SET, VALUE_A,
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput3() {
        getSubIterable("Set of values", CONVERT_LIST_TO_SET, VALUE_A, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput4() {
        getSubIterable("Set of values", CONVERT_LIST_TO_SET, VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput5() {
        getSubIterable("Set of values", CONVERT_LIST_TO_SET, VALUE_A,
                FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput6() {
        getSubIterable("Set of values", CONVERT_LIST_TO_SET, VALUE_A,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput7() {
        getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput8() {
        getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput9() {
        getIterable(CONVERT_LIST_TO_SET, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput10() {
        getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput11() {
        getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput2() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput3() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput4() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput5() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, FIVE_SECONDS,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput6() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput2() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput3() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                true, true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput4() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput5() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                FIVE_SECONDS, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput6() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput() {
        assertThat("Description", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).toString(),
                is("Set of values from (Set converted from list) on condition equals A ignore case. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput2() {
        assertThat("Description", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND).toString(),
                is("Set of values from (Set converted from list) on condition equals A ignore case. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput3() {
        assertThat("Description", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND).toString(),
                is("Set of values from (Set converted from list) on condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput4() {
        assertThat("Description", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).toString(),
                is("Set of values from (Set converted from list) on condition equals A ignore case. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput5() {
        assertThat("Description", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).toString(),
                is("Set of values from (Set converted from list) on condition equals A ignore case. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput6() {
        assertThat("Description", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A), true, true).toString(),
                is("Set of values from (Set converted from list) on condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput7() {
        assertThat("Description", getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET), FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                () -> NOTHING_WAS_FOUND).toString(),
                is("Set converted from list on condition as is. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput8() {
        assertThat("Description", getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET), FIVE_SECONDS, () -> NOTHING_WAS_FOUND).toString(),
                is("Set converted from list on condition as is. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput9() {
        assertThat("Description", getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET), () -> NOTHING_WAS_FOUND).toString(),
                is("Set converted from list on condition as is"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput10() {
        assertThat("Description", getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET), FIVE_SECONDS, FIVE_HUNDRED_MILLIS).toString(),
                is("Set converted from list on condition as is. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput11() {
        assertThat("Description", getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET), FIVE_SECONDS).toString(),
                is("Set converted from list on condition as is. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput() {
        Set<String> result;
        assertThat("Value", result = getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput2() {
        Set<String> result;
        assertThat("Value", result = getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput3() {
        Set<String> result;
        assertThat("Value", result = getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput4() {
        Set<String> result;
        assertThat("Value", result = getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput5() {
        Set<String> result;
        assertThat("Value", result = getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput6() {
        Set<String> result;
        assertThat("Value", result = getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput7() {
        Set<String> result;
        assertThat("Value", result = getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput8() {
        Set<String> result;
        assertThat("Value", result = getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput9() {
        Set<String> result;
        assertThat("Value", result = getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput10() {
        Set<String> result;
        assertThat("Value", result = getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput11() {
        Set<String> result;
        assertThat("Value", result = getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException2() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException3() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, false).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException2() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, false).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException3() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, false).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult2() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult3() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_W),
                true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult4() {
        assertThat("Value", getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult5() {
        assertThat("Value", getIterable(
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS,
                true,
                true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat("Value",  getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_W),
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS,
                true,
                true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
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
                getIterable(toGet("Set converted from list", CONVERT_LIST_TO_SET),
                FIVE_SECONDS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
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
                getIterable(toGet("Set converted from list", CONVERT_LIST_TO_SET),
                ONE_MILLISECOND,
                FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
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
            getSubIterable("Set of values",
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
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getIterable(
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
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getSubIterable("Set of values",
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
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getIterable(toGet("Set converted from list", CONVERT_LIST_TO_SET),
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
