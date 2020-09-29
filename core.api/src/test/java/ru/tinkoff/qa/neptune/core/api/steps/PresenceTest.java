package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.core.api.steps.StepFunction.toGet;

@SuppressWarnings("unchecked")
public class PresenceTest {

    private static final IllegalStateException IGNORED_EXCEPTION = new IllegalStateException("Ignored");
    private static final IllegalArgumentException IGNORED_EXCEPTION2 = new IllegalArgumentException("Ignored");
    private static final List<Class<? extends Throwable>> IGNORED_EXCEPTIONS = of(IllegalStateException.class,
            IllegalArgumentException.class);

    private static final IndexOutOfBoundsException EXPECTED_EXCEPTION_TO_BE_THROWN =
            new IndexOutOfBoundsException("Expected exception to be thrown");

    private static final Function<PresenceTestContext, Object> RETURNS_OBJECT = toGet("Object",
            presenceTestContext -> new Object());

    private static final Function<PresenceTestContext, Object> RETURNS_NULL = toGet("Null value",
            presenceTestContext -> null);

    private static final Function<PresenceTestContext, Object> RETURNS_OBJECT_ARRAY = toGet("Object array",
            presenceTestContext -> new Object[]{1, "String"});

    private static final Function<PresenceTestContext, Object> RETURNS_EMPTY_ARRAY = toGet("Empty array",
            presenceTestContext -> new String[]{});

    private static final Function<PresenceTestContext, Object> RETURNS_OBJECT_ITERABLE = toGet("Object iterable",
            presenceTestContext -> of(1, "String"));

    private static final Function<PresenceTestContext, Object> RETURNS_EMPTY_ITERABLE = toGet("Object iterable",
            presenceTestContext -> of());

    private static final Function<PresenceTestContext, Object> RETURNS_TRUE = toGet("True value",
            presenceTestContext -> true);

    private static final Function<PresenceTestContext, Object> RETURNS_FALSE = toGet("False value",
            presenceTestContext -> false);

    private static final Function<PresenceTestContext, Object> PRODUCES_IGNORED_EXCEPTIONS =
            presenceTestContext -> {
                List<RuntimeException> exceptions = of(IGNORED_EXCEPTION, IGNORED_EXCEPTION2);
                throw exceptions.get(new Random().nextInt(exceptions.size()));
            };

    private static final PresenceTestContext presenceTestContext = new PresenceTestContext();

    private static final Function<PresenceTestContext, Object> PRODUCES_EXPECTED_EXCEPTIONS = toGet("Expected exception",
            presenceTestContext -> {
                throw EXPECTED_EXCEPTION_TO_BE_THROWN;
            });

    @Test
    public void testOfFunctionWhichReturnsValue() {
        assertThat(presenceTestContext.presence(RETURNS_OBJECT),
                is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsNull() {
        assertThat(presenceTestContext.presence(RETURNS_NULL),
                is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsValue() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_OBJECT)),
                is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsNull() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_NULL)),
                is(false));
    }

    @Test
    public void testOfFunctionWhichReturnsArray() {
        assertThat(presenceTestContext.presence(RETURNS_OBJECT_ARRAY),
                is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsEmptyArray() {
        assertThat(presenceTestContext.presence(RETURNS_EMPTY_ARRAY),
                is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsArray() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_OBJECT_ARRAY)),
                is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsEmptyArray() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_EMPTY_ARRAY)),
                is(false));
    }

    @Test
    public void testOfFunctionWhichReturnsIterable() {
        assertThat(presenceTestContext.presence(RETURNS_OBJECT_ITERABLE),
                is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsEmptyIterable() {
        assertThat(presenceTestContext.presence(RETURNS_EMPTY_ITERABLE),
                is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsIterable() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_OBJECT_ITERABLE)),
                is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsEmptyIterable() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_EMPTY_ITERABLE)),
                is(false));
    }

    @Test
    public void testOfFunctionWhichReturnsTrue() {
        assertThat(presenceTestContext.presence(RETURNS_TRUE), is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsFalse() {
        assertThat(presenceTestContext.presence(RETURNS_FALSE), is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsTrue() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_TRUE)), is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsFalse() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_FALSE)), is(false));
    }

    @Test
    public void testOfFunctionWhichThrowsIgnoredException() {
        assertThat(presenceTestContext.presence(PRODUCES_IGNORED_EXCEPTIONS,
                IGNORED_EXCEPTIONS.toArray(new Class[]{})),
                is(false));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Expected exception to be thrown")
    public void testOfFunctionWhichThrowsExpectedException() {
        assertThat(presenceTestContext.presence(PRODUCES_EXPECTED_EXCEPTIONS), is(false));
    }

    @Test
    public void testOfGetSupplierWhichThrowsIgnoredException() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(PRODUCES_IGNORED_EXCEPTIONS),
                IGNORED_EXCEPTIONS),
                is(false));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Expected exception to be thrown")
    public void testOfGetSupplierWhichThrowsExpectedException() {
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(PRODUCES_EXPECTED_EXCEPTIONS),
                IGNORED_EXCEPTIONS),
                is(false));
    }

    @Test(expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = "Test exception")
    public void testOfThrowingExceptionIfNotPresentFunction() {
        var errorToThrow = new IllegalStateException("Test exception");
        assertThat(presenceTestContext.presence(RETURNS_NULL,
                () -> errorToThrow),
                is(false));
    }

    @Test(expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = "Test exception")
    public void testOfThrowingExceptionIfNotPresentSupplier() {
        var errorToThrow = new IllegalStateException("Test exception");
        assertThat(presenceTestContext.presenceOf(new TestGetSupplier(RETURNS_NULL),
                () -> errorToThrow),
                is(false));
    }

    private static class TestGetSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<PresenceTestContext, Object, TestGetSupplier> {
        TestGetSupplier(Function<PresenceTestContext, Object> originalFunction) {
            super(originalFunction.toString(), originalFunction);
        }
    }

    private static class PresenceTestContext extends Context<PresenceTestContext> {

        boolean presence(Function<PresenceTestContext, ?> toBePresent,
                         Supplier<? extends RuntimeException> exceptionSupplier,
                         Class<? extends Throwable>... toIgnore) {
            return super.presenceOf(toBePresent, exceptionSupplier, toIgnore);
        }

        /**
         * Retrieves whenever some object is present or not.
         *
         * @param toBePresent is a function that retrieves a value
         * @param toIgnore    which exceptions should be ignored during evaluation of {@code toBePresent}
         * @return is desired object present or not
         */
        boolean presence(Function<PresenceTestContext, ?> toBePresent,
                         Class<? extends Throwable>... toIgnore) {
            return super.presenceOf(toBePresent, toIgnore);
        }

        protected boolean presenceOf(SequentialGetStepSupplier<PresenceTestContext, ?, ?, ?, ?> toBePresent) {
            return super.presenceOf(toBePresent);
        }

        protected boolean presenceOf(SequentialGetStepSupplier<PresenceTestContext, ?, ?, ?, ?> toBePresent,
                                     Collection<Class<? extends Throwable>> toIgnore) {
            return super.presenceOf(toBePresent, toIgnore.toArray(new Class[]{}));
        }

        protected final boolean presenceOf(SequentialGetStepSupplier<PresenceTestContext, ?, ?, ?, ?> toBePresent,
                                           Supplier<? extends RuntimeException> exceptionSupplier) {
            return super.presenceOf(toBePresent, exceptionSupplier);
        }
    }
}
