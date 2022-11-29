package ru.tinkoff.qa.neptune.allure.jupiter.bridge;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static java.lang.Thread.currentThread;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class BasePreparing {

    @BeforeAll
    public static void beforeAllBase() {
        $("Thread " + currentThread(), () -> {});
    }

    @BeforeEach
    public void beforeEachBase() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterAll
    public static void afterAllBase() {
        $("Thread " + currentThread(), () -> {});
    }

    @AfterEach
    public void afterEachBase() {
        $("Thread " + currentThread(), () -> {});
    }
}
