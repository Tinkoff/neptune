package com.github.toy.constructor.core.api.conditional;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.ToGetSubIterable.getIterable;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetSubIterableTest extends BaseConditionalTest {

    private static final String SET_CONVERTED_FROM_LIST = "Set converted from list";

    @Test
    public void testOfDescriptionForFunctionWithIterableInputAndIterableOutput() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).toString(),
                is(format("%s. Criteria: %s", SET_CONVERTED_FROM_LIST, "equals " + A_UPPER + " ignore case")));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput2() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput3() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput4() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                Matchers.contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput5() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                Matchers.contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput6() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", VALUE_A),
                true, true).apply(LITERAL_LIST),
                Matchers.contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput7() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput8() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput9() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput10() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput11() {
        Set<String> result;
        assertThat(result = getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException2() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException3() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, false).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException2() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                FIVE_SECONDS, true, false).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "java.lang.RuntimeException was caught. Message: Exception for the unit testing!")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithoutIgnoringException3() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals " + A_UPPER + " ignore case", MALFORMED_PREDICATE),
                true, false).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult2() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals W ignore case", VALUE_W),
                FIVE_SECONDS, true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult3() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                condition("equals W ignore case", VALUE_W),
                true, true).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult4() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult5() {
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
                FIVE_SECONDS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
        assertThat(getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
            getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
            getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
            getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
            getIterable(SET_CONVERTED_FROM_LIST, CONVERT_LIST_TO_SET,
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
