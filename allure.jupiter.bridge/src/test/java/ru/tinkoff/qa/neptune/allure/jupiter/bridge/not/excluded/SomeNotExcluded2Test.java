package ru.tinkoff.qa.neptune.allure.jupiter.bridge.not.excluded;

import org.junit.jupiter.api.*;

import static java.lang.Thread.currentThread;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

class SomeNotExcluded2Test {

    @BeforeAll
    static void beforeAll2() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @AfterAll
    static void afterAll2() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @BeforeEach
    void beforeEach2() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @AfterEach
    void afterEach2() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @AfterEach
    void afterEachExcluded2() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Test
    void test21() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

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
