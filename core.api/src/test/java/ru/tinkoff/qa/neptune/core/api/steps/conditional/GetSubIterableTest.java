package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetList.getList;

public class GetSubIterableTest extends BaseConditionalTest {

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput2() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput3() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_A, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput4() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput5() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_A, FIVE_SECONDS).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput6() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_A).apply(LITERAL_LIST),
                contains(A_LOWER, A_UPPER));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput7() {
        assertThat(getList(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput8() {
        assertThat(getList(CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput9() {
        assertThat(getList(CONVERT_LIST_TO_SET, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput10() {
        assertThat(getList(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutput11() {
        assertThat(getList(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(LITERAL_LIST),
                contains(LITERAL_LIST.toArray()));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputIgnoringException() {
        assertThat(getList(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputIgnoringException2() {
        assertThat(getList(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE, FIVE_SECONDS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputIgnoringException3() {
        assertThat(getList(CONVERT_LIST_TO_SET, MALFORMED_PREDICATE).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputWithEmptyResult() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputWithEmptyResult2() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputWithEmptyResult3() {
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_W).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputWithEmptyResult4() {
        assertThat(getList(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputWithEmptyResult5() {
        assertThat(getList(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckTimeOut() {
        long start = System.currentTimeMillis();
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckSleeping() {
        long start = System.currentTimeMillis();
        assertThat(getList(CONVERT_LIST_TO_SET, VALUE_W, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(LITERAL_LIST),
                emptyCollectionOf(String.class));
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckTimeOut2() {
        long start = System.currentTimeMillis();
        assertThat(getList(CONVERT_LIST_TO_SET, FIVE_SECONDS).apply(EMPTY_LIST), emptyCollectionOf(String.class));
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_SECONDS.toMillis(),
                lessThanOrEqualTo(200L));
    }

    @Test
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckSleeping2() {
        long start = System.currentTimeMillis();
        assertThat(getList(CONVERT_LIST_TO_SET, ONE_MILLISECOND, FIVE_HUNDRED_MILLIS).apply(EMPTY_LIST),
                emptyCollectionOf(String.class));
        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(FIVE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - FIVE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "nothing was found")
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckTimeOutAndException() {
        long start = System.currentTimeMillis();
        try {
            getList(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckTimeOutAndException2() {
        long start = System.currentTimeMillis();
        try {
            getList(CONVERT_LIST_TO_SET, FIVE_SECONDS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
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
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckTimeOutWithSleepAndException() {
        long start = System.currentTimeMillis();
        try {
            getList(CONVERT_LIST_TO_SET, VALUE_W, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(LITERAL_LIST);
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
    public void testOfApplyingOfFunctionWithIterableInputAndListOutputToCheckTimeOutWithSleepAndException2() {
        long start = System.currentTimeMillis();
        try {
            getList(CONVERT_LIST_TO_SET, FIVE_SECONDS, FIVE_HUNDRED_MILLIS, () -> NOTHING_WAS_FOUND).apply(EMPTY_LIST);
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
