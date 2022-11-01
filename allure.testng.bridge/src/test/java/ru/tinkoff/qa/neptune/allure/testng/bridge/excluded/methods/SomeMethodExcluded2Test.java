package ru.tinkoff.qa.neptune.allure.testng.bridge.excluded.methods;

import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

import static java.lang.Thread.currentThread;
import static org.testng.Assert.assertTrue;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeMethodExcluded2Test {

    @BeforeClass
    public static void beforeAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterClass
    public static void afterAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @BeforeMethod
    public void beforeEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterMethod
    public void afterEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @AfterMethod
    public void afterEachExcluded2() {
        $("Thread " + currentThread(), () -> {});
    }

    @ExcludeFromAllureReport
    @Test
    public void test21() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @ExcludeFromAllureReport
    @Test
    public void test22() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }


    @Test
    public void test23() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Test
    public void test24() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }

    @Test
    public void test25() {
        $("Thread " + currentThread(), () -> assertTrue(true));
    }
}
