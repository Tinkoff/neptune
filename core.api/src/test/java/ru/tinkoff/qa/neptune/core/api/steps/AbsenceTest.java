package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.AbsenceTest.TestGetSupplier.getTestSupplier;

public class AbsenceTest {

    private static final AbsenceTestContext testContext = new AbsenceTestContext();
    private static final Function<AbsenceTestContext, Object> FAILED_EXCEPTION = new Function<>() {
        @Override
        public Object apply(AbsenceTestContext absenceTestContext) {
            throw new RuntimeException("Test exception");
        }


        public String toString() {
            return "Failed value";
        }
    };

    @Test
    public void test1() {
        var start = currentTimeMillis();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsObject(ofSeconds(5)).run()), ofSeconds(10)),
                is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void test2() {
        var start = currentTimeMillis();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsObject(ofMillis(0)).run())
                        .timeOut(ofSeconds(5)),
                ofSeconds(10)),
                is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test(expectedExceptions = StillPresentException.class)
    public void test3() {
        var start = currentTimeMillis();
        try {
            assertThat(testContext.absenceOrThrow(getTestSupplier(new FunctionThatReturnsObject(ofSeconds(10)).run()), ofSeconds(5)),
                    is(false));
        } catch (Throwable t) {
            var end = currentTimeMillis();
            assertThat(new BigDecimal(end - start),
                    closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
            assertThat(t.getMessage(), containsString("Still present: TestGetSupplierDescription"));
            throw t;
        }
        fail("Exception was expected");
    }

    @Test
    public void test4() {
        var start = currentTimeMillis();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsArray(ofSeconds(5)).run()), ofSeconds(10)),
                is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void test5() {
        var start = currentTimeMillis();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsArray(ofMillis(0)).run())
                        .timeOut(ofSeconds(5)),
                ofSeconds(10)), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test(expectedExceptions = StillPresentException.class)
    public void test6() {
        var start = currentTimeMillis();
        try {
            assertThat(testContext.absenceOrThrow(getTestSupplier(new FunctionThatReturnsArray(ofSeconds(10)).run()), ofSeconds(5)),
                    is(false));
        } catch (Throwable t) {
            var end = currentTimeMillis();
            assertThat(new BigDecimal(end - start),
                    closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
            assertThat(t.getMessage(), containsString("Still present: TestGetSupplierDescription"));
            throw t;
        }
        fail("Exception was expected");
    }

    @Test
    public void test7() {
        var start = currentTimeMillis();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsIterable(ofSeconds(5)).run()),
                ofSeconds(10)),
                is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void test8() {
        var start = currentTimeMillis();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsIterable(ofMillis(0)).run())
                        .timeOut(ofSeconds(5)),
                ofSeconds(10)), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test(expectedExceptions = StillPresentException.class)
    public void test9() {
        var start = currentTimeMillis();
        try {
            assertThat(testContext.absenceOrThrow(getTestSupplier(new FunctionThatReturnsIterable(ofSeconds(10)).run()), ofSeconds(5)),
                    is(false));
        } catch (Throwable t) {
            var end = currentTimeMillis();
            assertThat(new BigDecimal(end - start),
                    closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
            assertThat(t.getMessage(), containsString("Still present: TestGetSupplierDescription"));
            throw t;
        }
        fail("Exception was expected");
    }

    @Test
    public void test10() {
        var start = currentTimeMillis();
        assertThat(testContext.absence(getTestSupplier(FAILED_EXCEPTION)
                        .timeOut(ofSeconds(100)),
                ofSeconds(5)), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void test11() {
        PresenceSuccessCaptor.CAUGHT.clear();
        AbcnceSuccessCaptor.CAUGHT.clear();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsArray(ofSeconds(5)).run()), ofSeconds(10)),
                is(true));

        assertThat(PresenceSuccessCaptor.CAUGHT, empty());
        assertThat(AbcnceSuccessCaptor.CAUGHT, empty());
    }

    @Test
    public void test12() {
        PresenceSuccessCaptor.CAUGHT.clear();
        AbcnceSuccessCaptor.CAUGHT.clear();
        assertThat(testContext.absence(getTestSupplier(new FunctionThatReturnsObject(ofSeconds(10)).run()), ofSeconds(5)),
                is(false));

        assertThat(PresenceSuccessCaptor.CAUGHT, contains(containsString("Present:")));
        assertThat(AbcnceSuccessCaptor.CAUGHT, empty());
    }

    private static class FunctionThatReturnsObject implements Function<AbsenceTestContext, Object> {

        private static final Object DEFAULT_OBJECT = new Object();
        private final Duration timeToDisappearAfter;
        private Object toBeReturned = DEFAULT_OBJECT;

        private final Thread threadThatMakesObjectDisappear = new Thread(new Runnable() {
            @Override
            public void run() {
                var durationInMillis = timeToDisappearAfter.toMillis();
                var startTime = currentTimeMillis();
                while (currentTimeMillis() <= startTime + durationInMillis) {
                    toBeReturned = DEFAULT_OBJECT;
                }
                toBeReturned = null;
            }
        });

        private FunctionThatReturnsObject run() {
            threadThatMakesObjectDisappear.start();
            return this;
        }

        private FunctionThatReturnsObject(Duration timeToDisapearAfter) {
            this.timeToDisappearAfter = timeToDisapearAfter;
        }

        @Override
        public Object apply(AbsenceTestContext absencTestContext) {
            return toBeReturned;
        }

        @Override
        public String toString() {
            return "Some object";
        }
    }

    private static class FunctionThatReturnsArray implements Function<AbsenceTestContext, Object[]> {

        private static final Object[] DEFAULT_OBJECTS = new Object[]{1, 2, 3, 4, "Test"};
        private final Duration timeToDisappearAfter;
        private Object[] toBeReturned = DEFAULT_OBJECTS;

        private FunctionThatReturnsArray(Duration timeToDisappearAfter) {
            this.timeToDisappearAfter = timeToDisappearAfter;
        }

        private final Thread threadThatMakesObjectDisappear = new Thread(new Runnable() {
            @Override
            public void run() {
                var durationInMillis = timeToDisappearAfter.toMillis();
                var startTime = currentTimeMillis();
                while (currentTimeMillis() <= startTime + durationInMillis) {
                    toBeReturned = DEFAULT_OBJECTS;
                }
                toBeReturned = new Object[]{};
            }
        });

        private FunctionThatReturnsArray run() {
            threadThatMakesObjectDisappear.start();
            return this;
        }

        @Override
        public Object[] apply(AbsenceTestContext absenceTestContext) {
            return toBeReturned;
        }

        @Override
        public String toString() {
            return "Some array";
        }
    }

    private static class FunctionThatReturnsIterable implements Function<AbsenceTestContext, Iterable<Object>> {

        private static final Iterable<Object> DEFAULT_OBJECTS = List.of(1, 2, 3, 4, "Test");
        private final Duration timeToDisappearAfter;
        private Iterable<Object> toBeReturned = DEFAULT_OBJECTS;

        private final Thread threadThatMakesObjectDisappear = new Thread(new Runnable() {
            @Override
            public void run() {
                var durationInMillis = timeToDisappearAfter.toMillis();
                var startTime = currentTimeMillis();
                while (currentTimeMillis() <= startTime + durationInMillis) {
                    toBeReturned = DEFAULT_OBJECTS;
                }
                toBeReturned = List.of();
            }
        });

        private FunctionThatReturnsIterable run() {
            threadThatMakesObjectDisappear.start();
            return this;
        }

        private FunctionThatReturnsIterable(Duration timeToDisappearAfter) {
            this.timeToDisappearAfter = timeToDisappearAfter;
        }

        @Override
        public Iterable<Object> apply(AbsenceTestContext absenceTestContext) {
            return toBeReturned;
        }

        @Override
        public String toString() {
            return "Some iterable";
        }
    }


    @CaptureOnSuccess(by = PresenceSuccessCaptor.class)
    @CaptureOnFailure(by = AbcnceSuccessCaptor.class)
    static class TestGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<AbsenceTestContext, T, TestGetSupplier<T>> {
        TestGetSupplier(Function<AbsenceTestContext, T> originalFunction) {
            super(originalFunction);
        }

        @Description("TestGetSupplierDescription")
        public static <T> TestGetSupplier<?> getTestSupplier(Function<AbsenceTestContext, T> originalFunction) {
            return new TestGetSupplier<>(originalFunction);
        }
    }

    private static class AbsenceTestContext extends Context<AbsenceTestContext> {

        protected boolean absence(SequentialGetStepSupplier<AbsenceTestContext, ?, ?, ?, ?> toBeAbsent,
                                  Duration timeOut) {
            return super.absenceOf(toBeAbsent, timeOut);
        }

        protected boolean absenceOrThrow(SequentialGetStepSupplier<AbsenceTestContext, ?, ?, ?, ?> toBeAbsent,
                                         Duration timeOut) {
            return super.absenceOfOrThrow(toBeAbsent, timeOut);
        }

        public String toString() {
            return "Absence context";
        }
    }
}
