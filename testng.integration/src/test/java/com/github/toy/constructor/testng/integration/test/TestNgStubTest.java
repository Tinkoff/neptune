package com.github.toy.constructor.testng.integration.test;

import org.testng.annotations.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestNgStubTest extends BaseTestNgIntegrationTest {

    static TestNgStubTest testNgStubTest;

    public TestNgStubTest() {
        testNgStubTest = this;
    }

    @BeforeSuite
    public static void beforeSuiteStatic2() {
        //does nothing
    }

    @BeforeSuite
    public void beforeSuiteObject2() {
        //does nothing
    }

    @BeforeTest
    public static void beforeTestStatic2() {
        //does nothing
    }

    @BeforeTest
    public void beforeTestObject2() {
        //does nothing
    }

    @BeforeClass
    public static void beforeClassStatic2() {
        //does nothing
    }

    @BeforeClass
    public void beforeClassObject2() {
        //does nothing
    }

    @BeforeMethod
    public static void beforeMethodStatic2() {
        //does nothing
    }

    @BeforeMethod
    public void beforeMethodObject2() {
        //does nothing
    }

    @Test
    public static void staticTest() {
        assertThat(true, is(true));
    }

    @Test(enabled = false)
    public void ignoredTest() {
       assertThat(true, is(true));
    }

    @Test
    public void someEmptyTest() {
        assertThat(false, is(false));
    }

    @Test
    public void someEmptyTest2() {
        assertThat(true, is(true));
    }

    @Ignore
    @Test
    public void ignoredTest2() {
        assertThat(true, is(true));
    }
}
