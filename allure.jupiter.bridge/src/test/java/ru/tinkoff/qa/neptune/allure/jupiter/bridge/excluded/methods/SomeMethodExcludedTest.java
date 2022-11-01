package ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.methods;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;
import ru.tinkoff.qa.neptune.allure.jupiter.bridge.BasePreparing;

import static java.lang.Thread.currentThread;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeMethodExcludedTest extends BasePreparing {

    @BeforeAll
    static void beforeAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    static void afterAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @BeforeEach
    void beforeEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @AfterEach
    void afterEachExcluded() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    void afterEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    void test1() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @ExcludeFromAllureReport
    @Test
    void test2() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }


    @Test
    void test3() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Test
    void test4() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Test
    void test5() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Nested
    class NestedTest {

        @ExcludeFromAllureReport
        @BeforeEach
        void beforeEachNested() {
            $("Thread " + currentThread(), () -> {});
        }

        @Test
        void nestedTest1() {
            $("Thread " + currentThread(), () -> assertTrue(true));
        }

        @Nested
        class InnerNestedTest {

            @BeforeEach
            void beforeEachInnerNested() {
                $("Thread " + currentThread(), () -> assertTrue(true));
            }

            @Test
            void innerNestedTest1() {
                $("Thread " + currentThread(), () -> assertTrue(true));
            }
        }
    }

    @ExcludeFromAllureReport
    @Test
    void test6() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @ExcludeFromAllureReport
    @Test
    void test7() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }


    @Test
    void test8() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }
}
