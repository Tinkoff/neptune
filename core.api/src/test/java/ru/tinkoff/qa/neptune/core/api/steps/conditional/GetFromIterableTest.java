package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromIterable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class GetFromIterableTest extends BaseConditionalTest {

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput2() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput3() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput4() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput5() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput6() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A).apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput7() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput8() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput9() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput10() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput11() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutput12() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET).apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException2() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE, FIVE_SECONDS).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputIgnoringException3() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult2() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult3() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_A).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult4() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult5() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputWithNullResult6() {
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_W, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
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
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET,
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
        MatcherAssert.assertThat(ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET,
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
            ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithIterableInputAndSingleOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            ToGetObjectFromIterable.getFromIterable(CONVERT_LIST_TO_SET,
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
