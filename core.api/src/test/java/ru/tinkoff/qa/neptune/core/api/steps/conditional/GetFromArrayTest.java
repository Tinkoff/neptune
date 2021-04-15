package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromArray.getFromArray;

public class GetFromArrayTest extends BaseConditionalTest {

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput2() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput3() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput4() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput5() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS).apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput6() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A).apply(LITERAL_LIST),
                Matchers.is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput7() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput8() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput9() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, () -> NOTHING_WAS_FOUND)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput10() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput11() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutput12() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputIgnoringException() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, MALFORMED_PREDICATE,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputIgnoringException2() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, MALFORMED_PREDICATE, FIVE_SECONDS).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputIgnoringException3() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, MALFORMED_PREDICATE).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult2() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A, FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult3() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_A).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult4() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult5() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
                FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputWithNullResult6() {
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_W, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
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
        MatcherAssert.assertThat(getFromArray(CONVERT_LIST_TO_ARRAY,
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
            getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromArray(CONVERT_LIST_TO_ARRAY, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getFromArray(CONVERT_LIST_TO_ARRAY, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithArrayInputAndSingleOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getFromArray(CONVERT_LIST_TO_ARRAY,
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
