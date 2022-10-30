package ru.tinkoff.qa.neptune.allure.testng.bridge.fully.excluded;

import org.testng.annotations.*;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomePackageExcluded3Test {

    @BeforeClass
    public static void beforeAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterClass
    public static void afterAll() {
        $("Thread " + currentThread(), () -> {});
    }

    @BeforeMethod
    public void beforeEach() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterMethod
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
