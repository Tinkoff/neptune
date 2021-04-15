package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSingleCheckedObject.getSingle;

public class GetASingleTest extends BaseConditionalTest {

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_ONE_NUMBER,
                FIVE_SECONDS,
                FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput2() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_ONE_NUMBER, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput3() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_ONE_NUMBER, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput4() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_ONE_NUMBER, FIVE_SECONDS, FIVE_HUNDRED_MILLIS)
                        .apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput5() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_ONE_NUMBER, FIVE_SECONDS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput6() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_ONE_NUMBER).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput7() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput8() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput9() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput10() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutput11() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, FIVE_SECONDS).apply(LITERAL_LIST),
                Matchers.is(ONE_NUM));
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, MALFORMED_PREDICATE, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException2() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, MALFORMED_PREDICATE, FIVE_SECONDS).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputIgnoringException3() {
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, MALFORMED_PREDICATE).apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult() {
        assertThat(getSingle(s -> "X", VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult2() {
        assertThat(getSingle(s -> "X", VALUE_W, FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult3() {
        assertThat(getSingle(s -> "X", VALUE_W).apply(EMPTY_LIST), nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult4() {
        assertThat(getSingle(s -> null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputWithNullResult5() {
        assertThat(getSingle(s -> null, FIVE_SECONDS).apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
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
        assertThat(getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_W, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
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
        assertThat(getSingle(s -> null, FIVE_SECONDS).apply(LITERAL_LIST),
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
        assertThat(getSingle(s -> null, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
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
            getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_W, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getSingle(s -> null, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
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
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getSingle(GET_FIRST_OBJECT_FROM_LIST, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND)
                    .apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithSingleInputAndSingleOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getSingle(s -> null, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
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
