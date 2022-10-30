package ru.tinkoff.qa.neptune.allure.jupiter.bridge.not.excluded;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.jupiter.bridge.BasePreparing;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeNotExcludedTest extends BasePreparing {

    @BeforeAll
    public static void beforeAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    public static void afterAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @BeforeEach
    public void beforeEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    public void afterEachExcluded() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    public void afterEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @Test
    public void test1() {
        $("Thread " + currentThread(), () -> {});
    }

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
}
