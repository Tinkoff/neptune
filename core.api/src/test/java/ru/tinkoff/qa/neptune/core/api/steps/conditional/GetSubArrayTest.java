package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubArray;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetSubArrayTest extends BaseConditionalTest {

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS,
                () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput2() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput3() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_A, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput4() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput5() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS).apply(LITERAL_LIST),
                arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput6() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_A).apply(LITERAL_LIST),
                arrayContaining(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput7() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput8() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput9() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput10() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutput11() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS).apply(LITERAL_LIST),
                arrayContaining(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, MALFORMED_PREDICATE, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException2() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, MALFORMED_PREDICATE, FIVE_SECONDS).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputIgnoringException3() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, MALFORMED_PREDICATE).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult2() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult3() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W).apply(LITERAL_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult4() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputWithEmptyResult5() {
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS).apply(EMPTY_LIST),
                emptyArray());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS).apply(EMPTY_LIST),
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
        MatcherAssert.assertThat(ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
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
            ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
            ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(FIVE_SECONDS.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                    lessThanOrEqualTo(650L));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(FIVE_SECONDS.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                    lessThanOrEqualTo(650L));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithIterableInputAndIterableOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            ToGetSubArray.getArray(CONVERT_LIST_TO_ARRAY,
                    FIVE_SECONDS,
                    FIVE_HUNDRED_MILLIS,
                    () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(FIVE_SECONDS.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                    lessThanOrEqualTo(650L));
            throw e;
        }
        fail("The exception throwing was expected");
    }
}
