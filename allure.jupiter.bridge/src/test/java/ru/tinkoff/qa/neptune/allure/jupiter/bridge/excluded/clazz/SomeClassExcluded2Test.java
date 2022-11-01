package ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.clazz;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

import static java.lang.Thread.currentThread;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

@ExcludeFromAllureReport
public class SomeClassExcluded2Test {

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
}
