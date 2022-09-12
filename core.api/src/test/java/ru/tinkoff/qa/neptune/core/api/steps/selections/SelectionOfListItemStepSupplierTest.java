package ru.tinkoff.qa.neptune.core.api.steps.selections;

import com.google.common.collect.Iterables;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;
import ru.tinkoff.qa.neptune.core.api.steps.InvalidStepResultException;
import ru.tinkoff.qa.neptune.core.api.steps.NotPresentException;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.TestCapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.isEqual;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfListItemStepSupplierTest.TestListStepSupplier.testListStepSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionsPreparations.OBJECT_LIST;

public class SelectionOfListItemStepSupplierTest {

    @BeforeClass
    public static void beforeClass() {
        DO_CAPTURES_OF_INSTANCE.accept(CapturedEvents.SUCCESS_AND_FAILURE);
    }

    @AfterClass
    public static void afterClass() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @BeforeMethod
    public static void beforeMethods() {
        TestCapturedStringInjector.messages.clear();
    }

    @Test
    public void positiveTestWithoutIndex() {

        long start = System.currentTimeMillis();
        var result = testListStepSupplier()
                .criteria("Is object", Objects::nonNull)
                .returnIfEntireSize(isEqual(8))
                .returnOnCondition("contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG")))
                .timeOut(ofSeconds(1))
                .get()
                .apply(new Object());
        long end = System.currentTimeMillis();

        assertThat(result, equalTo(1));

        assertThat("Spent time in millis",
                end - start,
                lessThan(500L));

        assertThat(TestCapturedStringInjector.messages, emptyIterable());
    }

    @Test
    public void positiveTestWithIndex() {

        long start = System.currentTimeMillis();
        var result = testListStepSupplier()
                .criteria("Is object", Objects::nonNull)
                .returnItemOfIndex(1)
                .returnIfEntireSize(isEqual(8))
                .returnOnCondition("contains a list",
                        objects -> Iterables.contains(objects, List.of(2,
                                true,
                                false,
                                "ABCD",
                                "EFG")))
                .timeOut(ofSeconds(1))
                .get()
                .apply(new Object());
        long end = System.currentTimeMillis();

        assertThat(result, equalTo(2));

        assertThat("Spent time in millis",
                end - start,
                lessThan(500L));

        assertThat(TestCapturedStringInjector.messages, emptyIterable());
    }

    @Test
    public void negativeTest1() {
        long start = System.currentTimeMillis();

        try {
            testListStepSupplier()
                    .criteria("Is object", Objects::nonNull)
                    .returnIfEntireSize(isEqual(10))
                    .returnOnCondition("contains a list",
                            objects -> Iterables.contains(objects, List.of(2,
                                    true,
                                    false,
                                    "ABCD",
                                    "EFG")))
                    .timeOut(ofSeconds(1))
                    .throwOnNoResult()
                    .get()
                    .apply(new Object());
        } catch (Exception e) {
            long end = System.currentTimeMillis();

            assertThat(e, instanceOf(InvalidStepResultException.class));
            assertThat(e.getCause(), nullValue());

            assertThat("Spent time in millis",
                    end - start,
                    greaterThan(1000L));

            assertThat(TestCapturedStringInjector.messages, contains(startsWith("Got items")));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void negativeTest2() {
        long start = System.currentTimeMillis();

        try {
            testListStepSupplier(o -> {
                throw new IllegalStateException("Test Exception");
            })
                    .criteria("Is object", Objects::nonNull)
                    .returnIfEntireSize(isEqual(10))
                    .returnOnCondition("contains a list",
                            objects -> Iterables.contains(objects, List.of(2,
                                    true,
                                    false,
                                    "ABCD",
                                    "EFG")))
                    .timeOut(ofSeconds(1))
                    .throwOnNoResult()
                    .get()
                    .apply(new Object());
        } catch (Exception e) {
            long end = System.currentTimeMillis();

            assertThat(e, instanceOf(IllegalStateException.class));
            assertThat("Spent time in millis",
                    end - start,
                    lessThan(1000L));

            assertThat(TestCapturedStringInjector.messages, emptyIterable());
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void negativeTest3() {
        long start = System.currentTimeMillis();

        try {
            testListStepSupplier(o -> new ArrayList<>())
                    .criteria("Is object", Objects::nonNull)
                    .returnIfEntireSize(isEqual(10))
                    .returnOnCondition("contains a list",
                            objects -> Iterables.contains(objects, List.of(2,
                                    true,
                                    false,
                                    "ABCD",
                                    "EFG")))
                    .timeOut(ofSeconds(1))
                    .get()
                    .apply(new Object());
        } catch (Exception e) {
            long end = System.currentTimeMillis();

            assertThat(e, instanceOf(InvalidStepResultException.class));
            assertThat(e.getCause(), nullValue());
            assertThat("Spent time in millis",
                    end - start,
                    greaterThan(1000L));

            assertThat(TestCapturedStringInjector.messages, emptyIterable());
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void negativeTest4() {
        long start = System.currentTimeMillis();

        try {
            testListStepSupplier(o -> new ArrayList<>())
                    .criteria("Is object", Objects::nonNull)
                    .returnIfEntireSize(isEqual(10))
                    .returnOnCondition("contains a list",
                            objects -> Iterables.contains(objects, List.of(2,
                                    true,
                                    false,
                                    "ABCD",
                                    "EFG")))
                    .timeOut(ofSeconds(1))
                    .throwOnNoResult()
                    .get()
                    .apply(new Object());
        } catch (Exception e) {
            long end = System.currentTimeMillis();

            assertThat(e, instanceOf(InvalidStepResultException.class));
            assertThat(e.getCause(), nullValue());
            assertThat("Spent time in millis",
                    end - start,
                    greaterThan(1000L));

            assertThat(TestCapturedStringInjector.messages, emptyIterable());
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void negativeTest5() {
        long start = System.currentTimeMillis();

        try {
            testListStepSupplier(o -> {
                throw new IllegalStateException("Test Exception");
            })
                    .criteria("Is object", Objects::nonNull)
                    .returnIfEntireSize(isEqual(10))
                    .returnOnCondition("contains a list",
                            objects -> Iterables.contains(objects, List.of(2,
                                    true,
                                    false,
                                    "ABCD",
                                    "EFG")))
                    .timeOut(ofSeconds(1))
                    .throwOnNoResult()
                    .addIgnored(Exception.class)
                    .get()
                    .apply(new Object());
        } catch (Exception e) {
            long end = System.currentTimeMillis();

            assertThat(e, instanceOf(NotPresentException.class));
            assertThat(e.getCause(), instanceOf(IllegalStateException.class));
            assertThat("Spent time in millis",
                    end - start,
                    greaterThan(1000L));

            assertThat(TestCapturedStringInjector.messages, emptyIterable());
            return;
        }

        fail("Exception was expected");
    }

    @Description("Test object")
    static class TestListStepSupplier
            extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<Object, Object, TestListStepSupplier> {

        protected TestListStepSupplier() {
            this(o -> OBJECT_LIST);
        }

        protected <S extends Iterable<Object>> TestListStepSupplier(Function<Object, S> f) {
            super(f);
        }

        static TestListStepSupplier testListStepSupplier() {
            return new TestListStepSupplier();
        }

        static TestListStepSupplier testListStepSupplier(Function<Object, List<Object>> f) {
            return new TestListStepSupplier(f);
        }

        @Override
        protected TestListStepSupplier timeOut(Duration timeOut) {
            return super.timeOut(timeOut);
        }

        @Override
        protected TestListStepSupplier addIgnored(Class<? extends Throwable> toBeIgnored) {
            return super.addIgnored(toBeIgnored);
        }
    }
}
