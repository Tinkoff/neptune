package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BaseJunit5IntegrationTest extends BaseJunit5Test {

    @BeforeAll
    public static void beforeAllStatic() {
        //does nothing
    }

    @BeforeAll
    public void beforeAllSObject() {
        //does nothing
    }

    @BeforeEach
    public void beforeEach() {
        //does nothing
    }
}
