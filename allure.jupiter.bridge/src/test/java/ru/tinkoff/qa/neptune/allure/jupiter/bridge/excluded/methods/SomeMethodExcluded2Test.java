package ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.methods;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

import static java.lang.Thread.currentThread;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

class SomeMethodExcluded2Test {

    @BeforeAll
    static void beforeAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    static void afterAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @BeforeEach
    void beforeEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    void afterEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @AfterEach
    void afterEachExcluded2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    void test21() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @ExcludeFromAllureReport
    @Test
    void test22() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }


    @Test
    void test23() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Test
    void test24() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Test
    void test25() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }
}
