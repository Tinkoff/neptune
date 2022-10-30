package ru.tinkoff.qa.neptune.allure.jupiter.bridge.not.excluded;

import org.junit.jupiter.api.*;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeNotExcluded2Test {

    @BeforeAll
    public static void beforeAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    public static void afterAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @BeforeEach
    public void beforeEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    public void afterEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    public void afterEachExcluded2() {
        $("Thread " + currentThread(), () -> {});
    }

    @Test
    public void test21() {
        $("Thread " + currentThread(), () -> {});
    }

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
