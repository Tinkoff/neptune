package ru.tinkoff.qa.neptune.allure.testng.bridge;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class BasePreparing {

    @BeforeClass
    public static void beforeAllBase() {
        $("Thread " + currentThread(), () -> {});
    }

    @BeforeMethod
    public void beforeEachBase() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterClass
    public static void afterAllBase() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterMethod
    public void afterEachBase() {
        $("Thread " + currentThread(), () -> {});
    }
}
