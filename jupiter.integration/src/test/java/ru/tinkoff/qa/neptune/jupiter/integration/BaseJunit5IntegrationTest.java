package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import static java.lang.Thread.currentThread;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BaseJunit5IntegrationTest {

    @BeforeAll
    public static void beforeAllStatic() {
        System.out.println("Static base beforeAll in " + currentThread());
    }

    @BeforeAll
    public void beforeAllSObject() {
        System.out.println("Object base beforeAll in " + currentThread());
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("Object base beforeEach in " + currentThread());
    }
}
