package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.steps.Absence.CommonAbsence.absenceOf;

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
    public void absenceOfAnObjectTest1() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new FunctionThatReturnsObject(ofSeconds(5)).run())
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnObjectTest2() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new FunctionThatReturnsObject(ofSeconds(10)).run())
                .timeOut(ofSeconds(5))), is(false));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnObjectTest3() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new TestGetSupplier<>(new FunctionThatReturnsObject(ofSeconds(5)).run()))
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnObjectTest4() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new TestGetSupplier<>(new FunctionThatReturnsObject(ofMillis(0)).run())
                .timeOut(ofSeconds(5)))
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnArrayTest1() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new FunctionThatReturnsArray(ofSeconds(5)).run())
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnArrayTest2() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new FunctionThatReturnsArray(ofSeconds(10)).run())
                .timeOut(ofSeconds(5))), is(false));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnArrayTest3() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new TestGetSupplier<>(new FunctionThatReturnsArray(ofSeconds(5)).run()))
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnArrayTest4() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new TestGetSupplier<>(new FunctionThatReturnsArray(ofMillis(0)).run())
                .timeOut(ofSeconds(5)))
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnIterableTest1() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new FunctionThatReturnsIterable(ofSeconds(5)).run())
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnIterableTest2() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new FunctionThatReturnsIterable(ofSeconds(10)).run())
                .timeOut(ofSeconds(5))), is(false));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnIterableTest3() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new TestGetSupplier<>(new FunctionThatReturnsIterable(ofSeconds(5)).run()))
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofSeconds(5).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void absenceOfAnIterableTest4() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new TestGetSupplier<>(new FunctionThatReturnsIterable(ofMillis(0)).run())
                .timeOut(ofSeconds(5)))
                .timeOut(ofSeconds(10))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void whenFunctionThrowsExceptionTest() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(new TestGetSupplier<>(FAILED_EXCEPTION)
                .timeOut(ofSeconds(100)))
                .timeOut(ofSeconds(5))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
    }

    @Test
    public void whenFunctionThrowsExceptionTest2() {
        var start = currentTimeMillis();
        assertThat(testContext.get(absenceOf(FAILED_EXCEPTION)
                .timeOut(ofSeconds(5))), is(true));
        var end = currentTimeMillis();
        assertThat(new BigDecimal(end - start),
                closeTo(new BigDecimal(ofMillis(0).toMillis()), new BigDecimal(500)));
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

        private static final Object[] DEFAULT_OBJECTS = new Object[] {1, 2, 3, 4, "Test"};
        private final Duration timeToDisappearAfter;
        private Object[] toBeReturned;

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

    private static class FunctionThatReturnsIterable implements Function<AbsenceTestContext,Iterable<Object>> {

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

    private static class TestGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<AbsenceTestContext, T, TestGetSupplier<T>> {
        TestGetSupplier(Function<AbsenceTestContext, T> originalFunction) {
            super(originalFunction.toString(), originalFunction);
        }
    }

    private static class AbsenceTestContext implements GetStepContext<AbsenceTest.AbsenceTestContext> {

    }
}
