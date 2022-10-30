package ru.tinkoff.qa.neptune.allure.testng.bridge.not.excluded;


import org.testng.annotations.*;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeNotExcluded2Test {

    @BeforeClass
    public static void beforeAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterClass
    public static void afterAll2() {
        $("Thread " + currentThread(), () -> {});
    }

    @BeforeMethod
    public void beforeEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterMethod
    public void afterEach2() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterMethod
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
