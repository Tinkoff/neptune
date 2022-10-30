package ru.tinkoff.qa.neptune.allure.jupiter.bridge.excluded.clazz;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

@ExcludeFromAllureReport
public class SomeClassExcluded2Test {

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
}
