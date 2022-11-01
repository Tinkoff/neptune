package ru.tinkoff.qa.neptune.allure.jupiter.bridge.not.excluded;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.jupiter.bridge.BasePreparing;

import static java.lang.Thread.currentThread;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeNotExcludedTest extends BasePreparing {

    @BeforeAll
    static void beforeAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    static void afterAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @BeforeEach
    void beforeEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    void afterEachExcluded() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    void afterEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @Test
    void test1() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

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

        @BeforeEach
        void beforeEachNested() {
            $("Thread " + currentThread(), () -> assertTrue(true));
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
}
