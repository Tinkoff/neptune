package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseTestNgIntegrationTest extends BaseTestNgTest {

    @BeforeSuite
    public static void beforeSuiteStatic() {
        //does nothing
    }

    @BeforeSuite
    public void beforeSuiteObject() {
        //does nothing
    }

    @BeforeTest
    public static void beforeTestStatic() {
        //does nothing
    }

    @BeforeTest
    public void beforeTestObject() {
        //does nothing
    }

    @BeforeClass
    public static void beforeClassStatic() {
        //does nothing
    }

    @BeforeClass
    public void beforeClassObject() {
        //does nothing
    }

    @BeforeMethod
    public static void beforeMethodStatic() {
        //does nothing
    }

    @BeforeMethod
    public void beforeMethodObject() {
        //does nothing
    }
}
