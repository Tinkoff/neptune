package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubIterable;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetSubIterableTest extends BaseConditionalTest {

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput2() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput3() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_A, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput4() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput5() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput6() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_A).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput7() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput8() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput9() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput10() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput11() {
        Set<String> result;
        assertThat(result = ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
        assertThat("Class of the resulted iterable",
                result.getClass(),
                is(HashSet.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException2() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE, FIVE_SECONDS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException3() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult2() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult3() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_W).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult4() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult5() {
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_W, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(EMPTY_LIST), emptyCollectionOf(String.class));
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckSleeping2() {
        long start = System.currentTimeMillis();
        MatcherAssert.assertThat(ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
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
            ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
        } catch (Exception e) {
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
            ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
        } catch (Exception e) {
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
            ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
        } catch (Exception e) {
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
            ToGetSubIterable.getIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
        } catch (Exception e) {
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
