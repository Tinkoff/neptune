package com.github.toy.constructor.core.api.test;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.*;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetConditionalTest {

    private static final String A_UPPER = "A";
    private static final String A_LOWER = "a";

    private static final Predicate<String> VALUE_A = A_UPPER::equalsIgnoreCase;
    private static final Predicate<String> VALUE_W = "W"::equalsIgnoreCase;
    private static final Predicate<String> MALFORMED_PREDICATE = s -> {
        throw new RuntimeException("Exception for the unit testing!");
    };

    private static final List<String> LITERAL_LIST = of(A_LOWER, "B", A_UPPER, "C", "c", "D");

    private final Function<List<String>, Set<String>> CONVERT_LIST_TO_SET = HashSet::new;
    private final Function<List<String>, String[]> CONVERT_LIST_TO_ARRAY = strings -> strings.toArray(new String[] {});
    private final Function<List<String>, String> GET_FIRST_OBJECT_FROM_LIST = strings -> strings.get(0);

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return iterable is not defined")
    public void negativeTestOfNullFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value", null, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return iterable is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value", CONVERT_LIST_TO_SET, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate which should be used as the condition to check value from iterable is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndSingleOutput() {
        getFromIterable("Value",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), VALUE_A,
                true, true);
        fail("The exception throwing was expected");
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
        fail("The exception throwing was expected");
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
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return array is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value", CONVERT_LIST_TO_ARRAY, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate which should be used as the condition to check value from array is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndSingleOutput() {
        getFromArray("Value",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                true, true);
        fail("The exception throwing was expected");
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
        fail("The exception throwing was expected");
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

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return object is not defined")
    public void negativeTestOfNullFunctionWithSingleInputAndSingleOutput() {
        getSingleOnCondition("Value", null, VALUE_A, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return object is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithSingleInputAndSingleOutput() {
        getSingleOnCondition("Value", GET_FIRST_OBJECT_FROM_LIST, VALUE_A, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate which should be used as the condition to check returned object is not defined.")
    public void negativeTestOfNullConditionForFunctionWithSingleInputAndSingleOutput() {
        getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), null,
                true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithSingleInputAndSingleOutput() {
        getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST), VALUE_A,
                true);
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithSingleInputAndSingleOutput() {
        assertThat("Description", getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true).toString(),
                is("Value from (The first object of the list) on condition equals A ignore case"));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput() {
        assertThat("Value", getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException() {
        assertThat("Value", getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Meassage: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithoutIgnoringException() {
        assertThat("Value", getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult() {
        assertThat("Value", getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals W ignore case", VALUE_W),
                true).apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return iterable is not defined")
    public void negativeTestOfNullFunctionWithIterableInputAndIterableOutput() {
        getSubIterable("Set of values", null, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return iterable is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithIterableInputAndIterableOutput() {
        getSubIterable("Set of values", CONVERT_LIST_TO_SET, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate which should be used as the condition to filter values from iterable is not defined.")
    public void negativeTestOfNullConditionForFunctionWithIterableInputAndIterableOutput() {
        getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET), null,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithIterableInputAndIterableOutput() {
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
                true, true).toString(),
                is("Set of values from (Set converted from list) on condition equals A ignore case"));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput() {
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
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Meassage: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, false).apply(LITERAL_LIST),
                nullValue());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult() {
        assertThat("Value", getSubIterable("Set of values",
                toGet("Set converted from list", CONVERT_LIST_TO_SET),
                condition("equals W ignore case", VALUE_W),
                true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return array is not defined")
    public void negativeTestOfNullFunctionWithArrayInputAndArrayOutput() {
        getSubArray("Array of values", null, VALUE_A, true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which should return array is not described. Use StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunctionWithArrayeInputAndArrayOutput() {
        getSubArray("Array of values", CONVERT_LIST_TO_ARRAY, VALUE_A, true, true);
        fail("The exception thrwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Predicate which should be used as the condition to filter values from array is not defined.")
    public void negativeTestOfNullConditionForFunctionWithArrayInputAndArrayOutput() {
        getSubArray("Array of values",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), null,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Condition is not described. Use StoryWriter.condition to describe it.")
    public void negativeTestOfNotDescribedConditionForFunctionWithArrayInputAndArrayOutput() {
        getSubArray("Array of values",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY), VALUE_A,
                true, true);
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfDescriptionForFunctionWithArrayInputAndArrayOutput() {
        assertThat("Description", getSubArray("Array of values",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).toString(),
                is("Array of values from (Array converted from list) on condition equals A ignore case"));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutput() {
        assertThat("Value", getSubArray("Array of values",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).apply(LITERAL_LIST),
                arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputIgnoringException() {
        assertThat("Value", getSubArray("Array of values",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Meassage: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputWithoutIgnoringException() {
        assertThat("Value", getSubArray("Array of values",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, false).apply(LITERAL_LIST),
                emptyArray());
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputWithEmptyResult() {
        assertThat("Value", getSubArray("Array of values",
                toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                condition("equals W ignore case", VALUE_W),
                true, true).apply(LITERAL_LIST),
                emptyArray());
    }
}
