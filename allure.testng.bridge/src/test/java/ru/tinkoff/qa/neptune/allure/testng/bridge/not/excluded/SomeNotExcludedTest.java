package ru.tinkoff.qa.neptune.allure.testng.bridge.not.excluded;

import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.allure.testng.bridge.BasePreparing;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeNotExcludedTest extends BasePreparing {

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
    public void afterEachExcluded() {
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

    public static class NestedTest {

        @BeforeMethod
        public void beforeEachNested() {
            $("Thread " + currentThread(), () -> {});
        }

        @Test
        public void nestedTest1() {
            $("Thread " + currentThread(), () -> {});
        }

        public static class InnerNestedTest {

            @BeforeMethod
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
