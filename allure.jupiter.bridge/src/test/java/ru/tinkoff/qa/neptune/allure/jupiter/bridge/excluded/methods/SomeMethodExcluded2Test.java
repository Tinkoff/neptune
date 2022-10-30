package ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.methods;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeMethodExcluded2Test {

    @BeforeAll
    public static void beforeAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    public static void afterAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @BeforeEach
    public void beforeEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    public void afterEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @AfterEach
    public void afterEachExcluded2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    public void test21() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    public void test22() {
        $("Thread " + currentThread(), () -> {});
    }


    @Test
    public void test23() {
        $("Thread " + currentThread(), () -> {});
    }

    @Test
    public void test24() {
        $("Thread " + currentThread(), () -> {});
    }

    @Test
    public void test25() {
        $("Thread " + currentThread(), () -> {});
    }
}
