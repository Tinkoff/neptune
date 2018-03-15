package com.github.toy.constructor.core.api.test;

import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.*;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetConditionalTest {

    private static Duration FIVE_SECONDS = ofSeconds(5);
    private static Duration FIVE_HUNDRED_MILLIS = ofMillis(500);
    private static Duration ONE_MILLISECOND = ofMillis(1);
    private static final String A_UPPER = "A";
    private static final String A_LOWER = "a";
    private static final RuntimeException NOTHING_WAS_FOUND = new RuntimeException("nothing was found");

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

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputCheckFullDescriptionWithTimeOut() {
        assertThat("Full description with defined timeout",
                getFromIterable("Value",
                        toGet("Set converted from list", CONVERT_LIST_TO_SET),
                        condition("equals W ignore case", VALUE_W),
                        FIVE_SECONDS,
                        FIVE_HUNDRED_MILLIS,
                        true,
                        true,
                        () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Set converted from list) on condition equals W " +
                        "ignore case. Time to get valuable result: 0:00:05:000"));
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

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputCheckFullDescriptionWithTimeOut() {
        assertThat("Full description with defined timeout",
                getFromArray("Value",
                        toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                        condition("equals W ignore case", VALUE_W),
                        FIVE_SECONDS,
                        FIVE_HUNDRED_MILLIS,
                        true,
                        true,
                        () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (Array converted from list) on condition equals W ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
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

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat("Value", getSingleOnCondition("Value",
                toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS,
                true).apply(LITERAL_LIST),
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
        assertThat("Value", getSingleOnCondition("Value",
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

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutAndException() {
        long start = System.currentTimeMillis();
        try {
            getSingleOnCondition("Value",
                    toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
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
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getSingleOnCondition("Value",
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

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputCheckFullDescriptionWithTimeOut() {
        assertThat("Full description with defined timeout",
                getSingleOnCondition("Value",
                        toGet("The first object of the list", GET_FIRST_OBJECT_FROM_LIST),
                        condition("equals W ignore case", VALUE_W),
                        FIVE_SECONDS,
                        FIVE_HUNDRED_MILLIS,
                        true,
                        () -> NOTHING_WAS_FOUND).toString(),
                is("Value from (The first object of the list) on condition equals W ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
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

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputCheckFullDescriptionWithTimeOut() {
        assertThat("Full description with defined timeout",
                getSubIterable("Set of values",
                        toGet("Set converted from list", CONVERT_LIST_TO_SET),
                        condition("equals W ignore case", VALUE_W),
                        FIVE_SECONDS,
                        FIVE_HUNDRED_MILLIS,
                        true,
                        true,
                        () -> NOTHING_WAS_FOUND).toString(),
                is("Set of values from (Set converted from list) on condition equals W ignore case. Time to get valuable result: 0:00:05:000"));
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

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat("Value", getSubArray("Array of values",
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
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat("Value",  getSubArray("Array of values",
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

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputToCheckTimeOutAndException() {
        long start = System.currentTimeMillis();
        try {
            getSubArray("Array of values",
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
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getSubArray("Array of values",
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

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndArrayOutputCheckFullDescriptionWithTimeOut() {
        assertThat("Full description with defined timeout",
                getSubArray("Array of values",
                        toGet("Array converted from list", CONVERT_LIST_TO_ARRAY),
                        condition("equals W ignore case", VALUE_W),
                        FIVE_SECONDS,
                        FIVE_HUNDRED_MILLIS,
                        true,
                        true,
                        () -> NOTHING_WAS_FOUND).toString(),
                is("Array of values from (Array converted from list) on condition equals W ignore case. " +
                        "Time to get valuable result: 0:00:05:000"));
    }
}
