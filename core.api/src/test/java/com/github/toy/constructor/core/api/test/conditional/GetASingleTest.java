package com.github.toy.constructor.core.api.test.conditional;

import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetASingleTest extends BaseConditionalTest {

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput() {
        getSingle(null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput2() {
        getSingle(null, VALUE_A, FIVE_SECONDS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput3() {
        getSingle(null, VALUE_A, true,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput4() {
        getSingle(null, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput5() {
        getSingle(null, VALUE_A, FIVE_SECONDS, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput6() {
        getSingle(null, VALUE_A, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput7() {
        getSingle(null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput8() {
        getSingle(null, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput9() {
        getSingle(null, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput10() {
        getSingle(null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not defined.")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput11() {
        getSingle(null, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput() {
        getSingle( GET_FIRST_OBJECT_FROM_LIST, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput2() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_A, FIVE_SECONDS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput3() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_A,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput4() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST,
                VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput5() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_A, FIVE_SECONDS, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput6() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_A, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput7() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput8() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput9() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST,  () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput10() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS, FIVE_HUNDRED_MILLIS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput11() {
        getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithSingleInputAndSingleOutput() {
        getSingle(toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithSingleInputAndSingleOutput2() {
        getSingle(toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), null, FIVE_SECONDS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithSingleInputAndSingleOutput3() {
        getSingle(toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), null,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithSingleInputAndSingleOutput4() {
        getSingle(toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), null,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithSingleInputAndSingleOutput5() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), null, FIVE_SECONDS, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate is not defined.")
    public void negativeTestOfNullConditionForFunctionWithSingleInputAndSingleOutput6() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), null, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithSingleInputAndSingleOutput() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithSingleInputAndSingleOutput2() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), VALUE_A, FIVE_SECONDS,
                true, () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithSingleInputAndSingleOutput3() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), VALUE_A,
                true,  () -> NOTHING_WAS_FOUND);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithSingleInputAndSingleOutput4() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithSingleInputAndSingleOutput5() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), VALUE_A, FIVE_SECONDS,
                true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithSingleInputAndSingleOutput6() {
        getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), VALUE_A, true);
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND).toString(),
                is("The first object of the list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput2() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, () -> NOTHING_WAS_FOUND).toString(),
                is("The first object of the list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput3() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, () -> NOTHING_WAS_FOUND).toString(),
                is("The first object of the list with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput4() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true).toString(),
                is("The first object of the list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput5() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true).toString(),
                is("The first object of the list with condition equals A ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput6() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true).toString(),
                is("The first object of the list with condition equals A ignore case"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput7() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).toString(),
                is("The first object of the list. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput8() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).toString(),
                is("The first object of the list. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput9() {
        assertThat("Description", getSingle(toGet("The first object of the list",
                GET_FIRST_OBJECT_FROM_LIST), () -> NOTHING_WAS_FOUND).toString(),
                is("The first object of the list"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput10() {
        assertThat("Description", getSingle(toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).toString(),
                is("The first object of the list. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput11() {
        assertThat("Description", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), FIVE_SECONDS).toString(),
                is("The first object of the list. Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput2() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput3() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                true, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput4() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true)
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput5() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                FIVE_SECONDS, true)
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput6() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_ONE_NUMBER),
                true).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput7() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput8() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput9() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput10() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput11() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                FIVE_SECONDS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException() {
        assertThat("Value", getSingle(
                toGet("Value", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException2() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException3() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithoutIgnoringException() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithoutIgnoringException2() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithoutIgnoringException3() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", s -> "X"),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult2() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", s -> "X"),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult3() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", s -> "X"),
                condition("equals W ignore case", VALUE_W), true).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult4() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", s -> null),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult5() {
        assertThat("Value", getSingle(
                toGet("The first object of the list", s -> null),
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
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
        assertThat("Value", getSingle(
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
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
        assertThat("Value", getSingle(
                toGet("The first object of the list", s -> null),
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
        assertThat("Value", getSingle(
                toGet("The first object of the list", s -> null),
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
            getSingle(
                    toGet("Value", GET_FIRST_OBJECT_FROM_LIST),
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
            getSingle(toGet("The first object of the list", s -> null),
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
            getSingle(
                    toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
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
            getSingle(toGet("The first object of the list", s -> null),
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
