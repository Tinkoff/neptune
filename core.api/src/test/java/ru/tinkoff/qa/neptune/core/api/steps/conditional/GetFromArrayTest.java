package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.core.api.steps.conditional.GetFromArrayTest.GetTestObjectStepSupplier.createStep;

public class GetFromArrayTest extends BaseConditionalConstants {

    @Test
    public void positiveTestWithResult() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .criteria("==A", VALUE_A)
                        .throwOnNoResult()
                        .get()
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void positiveTestWithResult2() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .criteria("==A", VALUE_A)
                        .throwOnNoResult()
                        .get()
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void positiveTestWithResult3() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .criteria("==A", VALUE_A)
                        .throwOnNoResult()
                        .get()
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void positiveTestWithResult4() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .criteria("==A", VALUE_A)
                        .get()
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void positiveTestWithResult5() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .criteria("==A", VALUE_A)
                        .get()
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void positiveTestWithResult6() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .criteria("==A", VALUE_A)
                        .get()
                        .apply(LITERAL_LIST),
                is(A_LOWER));
    }

    @Test
    public void positiveTestWithResult7() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .throwOnNoResult()
                        .get()
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void positiveTestWithResult8() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .throwOnNoResult()
                        .get()
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void positiveTestWithResult9() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .throwOnNoResult()
                        .get()
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void positiveTestWithResult10() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .get()
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void positiveTestWithResult11() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .get()
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void positiveTestWithResult12() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .get()
                        .apply(LITERAL_LIST),
                is(ONE_NUM));
    }

    @Test
    public void positiveTestWithNoResult() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .criteria("Something", MALFORMED_PREDICATE)
                        .get()
                        .apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult2() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .criteria("Something", MALFORMED_PREDICATE)
                        .get()
                        .apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult3() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .criteria("Something", MALFORMED_PREDICATE)
                        .get()
                        .apply(LITERAL_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult4() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .criteria("==A", VALUE_A)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult5() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .criteria("==A", VALUE_A)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult6() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .criteria("==A", VALUE_A)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult7() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult8() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResult9() {
        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());
    }

    @Test
    public void positiveTestWithNoResultAndTimeValidation() {
        long start = System.currentTimeMillis();

        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .criteria("==W", VALUE_W)
                        .get()
                        .apply(LITERAL_LIST),
                nullValue());

        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                lessThanOrEqualTo(70L));
    }

    @Test
    public void positiveTestWithNoResultAndTimeValidation2() {
        long start = System.currentTimeMillis();

        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_MILLISECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .criteria("==W", VALUE_W)
                        .get()
                        .apply(LITERAL_LIST),
                nullValue());

        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(ONE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - ONE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(70L));
    }

    @Test
    public void positiveTestWithNoResultAndTimeValidation3() {
        long start = System.currentTimeMillis();

        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_SECOND)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());


        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(ONE_SECOND.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                lessThanOrEqualTo(70L));
    }

    @Test
    public void positiveTestWithNoResultAndTimeValidation4() {
        long start = System.currentTimeMillis();

        assertThat(createStep(CONVERT_LIST_TO_ARRAY)
                        .timeOut(ONE_MILLISECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .get()
                        .apply(EMPTY_LIST),
                nullValue());

        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThanOrEqualTo(ONE_HUNDRED_MILLIS.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - ONE_HUNDRED_MILLIS.toMillis(),
                lessThanOrEqualTo(70L));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void negativeTestWithTimeValidation() {
        long start = System.currentTimeMillis();
        try {
            createStep(CONVERT_LIST_TO_ARRAY)
                    .timeOut(ONE_SECOND)
                    .criteria("==W", VALUE_W)
                    .throwOnNoResult()
                    .get()
                    .apply(LITERAL_LIST);

        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                    lessThanOrEqualTo(70L));
            assertThat(e, throwableHasMessage(containsString("nothing was found: Test Data")));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void negativeTestWithTimeValidation2() {
        long start = System.currentTimeMillis();
        try {
            createStep(CONVERT_LIST_TO_ARRAY)
                    .timeOut(ONE_SECOND)
                    .throwOnNoResult()
                    .get()
                    .apply(EMPTY_LIST);

        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                    lessThanOrEqualTo(70L));
            assertThat(e, throwableHasMessage(containsString("nothing was found: Test Data")));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void negativeTestWithTimeValidation3() {
        long start = System.currentTimeMillis();
        try {
            createStep(CONVERT_LIST_TO_ARRAY)
                    .timeOut(ONE_SECOND)
                    .pollingInterval(ONE_HUNDRED_MILLIS)
                    .criteria("==W", VALUE_W)
                    .throwOnNoResult()
                    .get()
                    .apply(LITERAL_LIST);
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(ONE_SECOND.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                    lessThanOrEqualTo(150L));
            assertThat(e, throwableHasMessage(containsString("nothing was found: Test Data")));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void negativeTestWithTimeValidation4() {
        long start = System.currentTimeMillis();
        try {
            createStep(CONVERT_LIST_TO_ARRAY)
                    .timeOut(ONE_SECOND)
                    .pollingInterval(ONE_HUNDRED_MILLIS)
                    .throwOnNoResult()
                    .get()
                    .apply(EMPTY_LIST);
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(ONE_SECOND.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                    lessThanOrEqualTo(150L));
            assertThat(e, throwableHasMessage(containsString("nothing was found: Test Data")));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void testOfThrowingOfNotIgnoredException() {
        createStep(createMalformedFunction())
                .throwOnNoResult()
                .get()
                .apply(LITERAL_LIST);
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void testOfThrowingOfNotIgnoredException2() {
        long start = System.currentTimeMillis();
        try {
            createStep(createMalformedFunction())
                    .throwOnNoResult()
                    .timeOut(ONE_SECOND)
                    .pollingInterval(ONE_HUNDRED_MILLIS)
                    .get()
                    .apply(LITERAL_LIST);
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    lessThanOrEqualTo(70L));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @Test
    public void testOfIgnoringException() {
        assertThat(createStep(createMalformedFunction())
                        .addIgnored(List.of(IllegalStateException.class))
                        .get()
                        .apply(LITERAL_LIST),
                nullValue());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testOfIgnoringException2() {
        createStep(createMalformedFunction())
                .addIgnored(List.of(IllegalStateException.class))
                .throwOnNoResult()
                .get()
                .apply(LITERAL_LIST);
    }

    @Test
    public void testOfIgnoringException3() {
        long start = System.currentTimeMillis();

        assertThat(createStep(createMalformedFunction())
                        .addIgnored(List.of(IllegalStateException.class))
                        .timeOut(ONE_SECOND)
                        .pollingInterval(ONE_HUNDRED_MILLIS)
                        .get()
                        .apply(LITERAL_LIST),
                nullValue());

        long end = System.currentTimeMillis();
        assertThat("Spent time in millis", end - start,
                greaterThan(ONE_SECOND.toMillis()));
        assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                lessThanOrEqualTo(150L));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testOfIgnoringException4() {
        long start = System.currentTimeMillis();

        try {
            assertThat(createStep(createMalformedFunction())
                            .addIgnored(List.of(IllegalStateException.class))
                            .timeOut(ONE_SECOND)
                            .pollingInterval(ONE_HUNDRED_MILLIS)
                            .throwOnNoResult()
                            .get()
                            .apply(LITERAL_LIST),
                    nullValue());
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            assertThat("Spent time in millis", end - start,
                    greaterThan(ONE_SECOND.toMillis()));
            assertThat("Difference between expected and actual duration", end - start - ONE_SECOND.toMillis(),
                    lessThanOrEqualTo(150L));
            assertThat(e, throwableHasMessage(containsString("nothing was found: Test Data")));
            throw e;
        }
        fail("The exception throwing was expected");
    }

    @ThrowWhenNoData(toThrow = RuntimeException.class, startDescription = "nothing was found:")
    @Description("Test Data")
    static class GetTestObjectStepSupplier<T, R> extends SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<T, R, GetTestObjectStepSupplier<T, R>> {

        protected GetTestObjectStepSupplier(Function<T, R[]> originalFunction) {
            super(originalFunction);
        }

        static <T, R> GetTestObjectStepSupplier<T, R> createStep(Function<T, R[]> originalFunction) {
            return new GetTestObjectStepSupplier<>(originalFunction);
        }

        @Override
        protected GetTestObjectStepSupplier<T, R> timeOut(Duration duration) {
            return super.timeOut(duration);
        }

        @Override
        protected GetTestObjectStepSupplier<T, R> pollingInterval(Duration duration) {
            return super.pollingInterval(duration);
        }

        @Override
        protected GetTestObjectStepSupplier<T, R> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
            return super.addIgnored(toBeIgnored);
        }
    }
}
