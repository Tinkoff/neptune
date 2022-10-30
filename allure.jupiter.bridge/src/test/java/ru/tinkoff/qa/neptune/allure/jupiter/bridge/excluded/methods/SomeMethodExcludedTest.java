package ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.methods;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;
import ru.tinkoff.qa.neptune.allure.jupiter.bridge.BasePreparing;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeMethodExcludedTest extends BasePreparing {

    @BeforeAll
    public static void beforeAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    public static void afterAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @BeforeEach
    public void beforeEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @AfterEach
    public void afterEachExcluded() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    public void afterEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    public void test1() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    public void test2() {
        $("Thread " + currentThread(), () -> {});
    }


    @Test
    public void test3() {
        $("Thread " + currentThread(), () -> {});
    }

    @Test
    public void test4() {
        $("Thread " + currentThread(), () -> {});
    }

    @Test
    public void test5() {
        $("Thread " + currentThread(), () -> {});
    }

    @Nested
    public class NestedTest {

        @ExcludeFromAllureReport
        @BeforeEach
        public void beforeEachNested() {
            $("Thread " + currentThread(), () -> {});
        }

        @Test
        public void nestedTest1() {
            $("Thread " + currentThread(), () -> {});
        }

        @Nested
        public class InnerNestedTest {

            @BeforeEach
            public void beforeEachInnerNested() {
                $("Thread " + currentThread(), () -> {});
            }

            @Test
            public void innerNestedTest1() {
                $("Thread " + currentThread(), () -> {});
            }
        }
    }

    @ExcludeFromAllureReport
    @Test
    public void test6() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    public void test7() {
        $("Thread " + currentThread(), () -> {});
    }


    @Test
    public void test8() {
        $("Thread " + currentThread(), () -> {});
    }
}
