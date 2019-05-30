package ru.tinkoff.qa.neptune.testng.integration.test;

import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseTestNgIntegrationTest extends BaseTestNgTest {

    private ContextClass1 stepClass1;
    private ContextClass2 stepClass2;
    private final ContextClass3 stepClass3 = null;
    private ContextClass4 stepClass4;

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

    ContextClass1 getStepClass1() {
        return stepClass1;
    }

    ContextClass2 getStepClass2() {
        return stepClass2;
    }

    ContextClass3 getStepClass3() {
        return stepClass3;
    }

    ContextClass4 getStepClass4() {
        return stepClass4;
    }
}
