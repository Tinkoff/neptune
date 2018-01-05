package com.github.toy.constructor.core.api.test;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getFromArray;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getFromIterable;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.fail;

public class GetConditionalTest {

    private static final String A_UPPER = "A";
    private static final String A_LOWER = "a";

    private static final Predicate<String> VALUE_A = A_UPPER::equalsIgnoreCase;
    private static final Predicate<String> VALUE_W = "W"::equalsIgnoreCase;
    private static final Predicate<String> MALFORMED_PREDICATE = s -> {
        throw new RuntimeException("Exception for the unit testing!");
    };

    private static final List<String> LITERAL_LIST = of(A_LOWER, "B", A_LOWER, "C", "c", "D");

    private final Function<List<String>, Set<String>> CONVERT_LIST_TO_SET = HashSet::new;
    private final Function<List<String>, String[]> CONVERT_LIST_TO_ARRAY = strings -> strings.toArray(new String[] {});

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return iterable is not defined")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value", null, VALUE_A, true, true);
        fail("The exception thowing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return iterable is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A, true, true);
        fail("The exception thowing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate which should be used as the condition to check value from iterable is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null,
                true, true);
        fail("The exception thowing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                true, true);
        fail("The exception thowing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndSingleOutput() {
        assertThat("Description", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true,
                true).toString(),
                is("Value from (Set converted from list) on condition equals A ignore case"));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true,
                true).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true,
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Meassage: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithoutIgnoringException() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception thowing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult() {
        assertThat("Value", getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_W),
                true,
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return array is not defined")
    public void negativeTestOfNullFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value", null, VALUE_A, true, true);
        fail("The exception thowing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return array is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A, true, true);
        fail("The exception thowing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate which should be used as the condition to check value from array is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                true, true);
        fail("The exception thowing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                true, true);
        fail("The exception thowing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndSingleOutput() {
        assertThat("Description", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true,
                true).toString(),
                is("Value from (Array converted from list) on condition equals A ignore case"));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true,
                true).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputIgnoringException() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true,
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Meassage: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithoutIgnoringException() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true,
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception thowing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult() {
        assertThat("Value", getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_W),
                true,
                true).apply(LITERAL_LIST),
                nullValue());
    }
}
