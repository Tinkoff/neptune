package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.steps.PresenceTest.TestGetArrayItemSupplier.getTestArrayItemSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.PresenceTest.TestGetArraySupplier.getTestArraySupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.PresenceTest.TestGetListItemSupplier.getTestListItemSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.PresenceTest.TestGetListSupplier.getTestListSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.PresenceTest.TestGetSupplier.getTestSupplier;

@SuppressWarnings("unchecked")
public class PresenceTest {

    private static final IllegalStateException IGNORED_EXCEPTION = new IllegalStateException("Ignored");
    private static final IllegalArgumentException IGNORED_EXCEPTION2 = new IllegalArgumentException("Ignored");
    private static final List<Class<? extends Throwable>> IGNORED_EXCEPTIONS = of(IllegalStateException.class,
            IllegalArgumentException.class);

    private static final IndexOutOfBoundsException EXPECTED_EXCEPTION_TO_BE_THROWN =
            new IndexOutOfBoundsException("Expected exception to be thrown");

    private static final Function<PresenceTestContext, Object> RETURNS_OBJECT = new Get<>("Object",
            presenceTestContext -> new Object());

    private static final Function<PresenceTestContext, Object> RETURNS_NULL = new Get<>("Null value",
            presenceTestContext -> null);

    private static final Function<PresenceTestContext, Object> RETURNS_OBJECT_ARRAY = new Get<>("Object array",
            presenceTestContext -> new Object[]{1, "String"});

    private static final Function<PresenceTestContext, Object> RETURNS_EMPTY_ARRAY = new Get<>("Empty array",
            presenceTestContext -> new String[]{});

    private static final Function<PresenceTestContext, Object> RETURNS_OBJECT_ITERABLE = new Get<>("Object iterable",
            presenceTestContext -> of(1, "String"));

    private static final Function<PresenceTestContext, Object> RETURNS_EMPTY_ITERABLE = new Get<>("Object iterable",
            presenceTestContext -> of());

    private static final Function<PresenceTestContext, Object> RETURNS_TRUE = new Get<>("True value",
            presenceTestContext -> true);

    private static final Function<PresenceTestContext, Object> RETURNS_FALSE = new Get<>("False value",
            presenceTestContext -> false);

    private static final Function<PresenceTestContext, Object> PRODUCES_IGNORED_EXCEPTIONS =
            presenceTestContext -> {
                List<RuntimeException> exceptions = of(IGNORED_EXCEPTION, IGNORED_EXCEPTION2);
                throw exceptions.get(new Random().nextInt(exceptions.size()));
            };

    private static final PresenceTestContext presenceTestContext = new PresenceTestContext();

    private static final Function<PresenceTestContext, Object> PRODUCES_EXPECTED_EXCEPTIONS = new Get<>("Expected exception",
            presenceTestContext -> {
                throw EXPECTED_EXCEPTION_TO_BE_THROWN;
            });

    @Test
    public void test1() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_OBJECT)),
                is(true));
    }

    @Test
    public void test2() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_NULL)),
                is(false));
    }

    @Test
    public void test3() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_OBJECT_ARRAY)),
                is(true));
    }

    @Test
    public void test4() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_EMPTY_ARRAY)),
                is(false));
    }

    @Test
    public void test5() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_OBJECT_ITERABLE)),
                is(true));
    }

    @Test
    public void test6() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_EMPTY_ITERABLE)),
                is(false));
    }

    @Test
    public void test7() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_TRUE)), is(true));
    }

    @Test
    public void test8() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_FALSE)), is(false));
    }

    @Test
    public void test9() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(PRODUCES_IGNORED_EXCEPTIONS),
                IGNORED_EXCEPTIONS),
                is(false));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Expected exception to be thrown")
    public void test10() {
        assertThat(presenceTestContext.presenceOf(getTestSupplier(PRODUCES_EXPECTED_EXCEPTIONS),
                IGNORED_EXCEPTIONS),
                is(false));
    }

    @Test(expectedExceptions = NotPresentException.class,
            expectedExceptionsMessageRegExp = "Not present: TestGetSupplierDescription")
    public void test11() {
        assertThat(presenceTestContext.presenceOfOrThrow(getTestSupplier(RETURNS_NULL)),
                is(false));
    }

    @Test
    public void test12() {
        PresenceSuccessCaptor.CAUGHT.clear();
        AbcnceSuccessCaptor.CAUGHT.clear();
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_OBJECT)),
                is(true));

        assertThat(PresenceSuccessCaptor.CAUGHT, contains(containsString("Present: ")));
        assertThat(AbcnceSuccessCaptor.CAUGHT, empty());
    }

    @Test
    public void test13() {
        PresenceSuccessCaptor.CAUGHT.clear();
        AbcnceSuccessCaptor.CAUGHT.clear();
        assertThat(presenceTestContext.presenceOf(getTestSupplier(RETURNS_EMPTY_ITERABLE)),
            is(false));

        assertThat(PresenceSuccessCaptor.CAUGHT, empty());
        assertThat(AbcnceSuccessCaptor.CAUGHT, empty());
    }

    @Test
    public void test14() {
        assertThat(presenceTestContext.presenceOf(getTestListSupplier(o -> List.of(1, 2, 3))
                .returnListOfSize(4)),
            is(true));
    }

    @Test
    public void test15() {
        assertThat(presenceTestContext.presenceOf(getTestArraySupplier(o -> new Object[]{1, 2, 3})
                .returnArrayOfLength(4)),
            is(true));
    }

    @Test
    public void test16() {
        assertThat(presenceTestContext.presenceOf(getTestListItemSupplier(o -> List.of(1, 2, 3))
                .returnItemOfIndex(4)),
            is(true));
    }

    @Test
    public void test17() {
        assertThat(presenceTestContext.presenceOf(getTestArrayItemSupplier(o -> new Object[]{1, 2, 3})
                .returnItemOfIndex(4)),
            is(true));
    }

    @CaptureOnSuccess(by = PresenceSuccessCaptor.class)
    @CaptureOnFailure(by = AbcnceSuccessCaptor.class)
    static class TestGetSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<PresenceTestContext, Object, TestGetSupplier> {
        TestGetSupplier(Function<PresenceTestContext, Object> originalFunction) {
            super(originalFunction);
        }

        @Description("TestGetSupplierDescription")
        public static TestGetSupplier getTestSupplier(Function<PresenceTestContext, Object> originalFunction) {
            return new TestGetSupplier(originalFunction);
        }
    }

    static class TestGetListSupplier extends SequentialGetStepSupplier.GetListStepSupplier<PresenceTestContext, List<Object>, Object, TestGetListSupplier> {

        protected TestGetListSupplier(Function<PresenceTestContext, List<Object>> originalFunction) {
            super(originalFunction);
        }

        @Description("TestGetSupplierDescription")
        public static TestGetListSupplier getTestListSupplier(Function<PresenceTestContext, List<Object>> originalFunction) {
            return new TestGetListSupplier(originalFunction);
        }
    }

    static class TestGetArraySupplier extends SequentialGetStepSupplier.GetArrayStepSupplier<PresenceTestContext, Object, TestGetArraySupplier> {

        protected TestGetArraySupplier(Function<PresenceTestContext, Object[]> originalFunction) {
            super(originalFunction);
        }

        @Description("TestGetSupplierDescription")
        public static TestGetArraySupplier getTestArraySupplier(Function<PresenceTestContext, Object[]> originalFunction) {
            return new TestGetArraySupplier(originalFunction);
        }
    }

    static class TestGetListItemSupplier extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<PresenceTestContext, Object, TestGetListItemSupplier> {

        protected <S extends Iterable<Object>> TestGetListItemSupplier(Function<PresenceTestContext, S> originalFunction) {
            super(originalFunction);
        }

        @Description("TestGetSupplierDescription")
        public static TestGetListItemSupplier getTestListItemSupplier(Function<PresenceTestContext, List<Object>> originalFunction) {
            return new TestGetListItemSupplier(originalFunction);
        }
    }

    static class TestGetArrayItemSupplier extends SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<PresenceTestContext, Object, TestGetArrayItemSupplier> {

        protected TestGetArrayItemSupplier(Function<PresenceTestContext, Object[]> originalFunction) {
            super(originalFunction);
        }

        @Description("TestGetSupplierDescription")
        public static TestGetArrayItemSupplier getTestArrayItemSupplier(Function<PresenceTestContext, Object[]> originalFunction) {
            return new TestGetArrayItemSupplier(originalFunction);
        }
    }

    private static class PresenceTestContext extends Context<PresenceTestContext> {

        protected boolean presenceOf(SequentialGetStepSupplier<PresenceTestContext, ?, ?, ?, ?> toBePresent) {
            return super.presenceOf(toBePresent);
        }

        protected boolean presenceOf(SequentialGetStepSupplier<PresenceTestContext, ?, ?, ?, ?> toBePresent,
                                     Collection<Class<? extends Throwable>> toIgnore) {
            return super.presenceOf(toBePresent, toIgnore.toArray(new Class[]{}));
        }

        protected final boolean presenceOfOrThrow(SequentialGetStepSupplier<PresenceTestContext, ?, ?, ?, ?> toBePresent) {
            return super.presenceOfOrThrow(toBePresent);
        }
    }
}
