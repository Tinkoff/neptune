package com.github.toy.constructor.testng.integration.test.ignored;

import com.github.toy.constructor.testng.integration.test.BaseTestNgIntegrationTest;
import org.testng.annotations.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Ignore
public class IgnoredStubTest extends BaseTestNgIntegrationTest {

    @BeforeSuite
    public static void beforeSuiteStatic3() {
        //does nothing
    }

    @BeforeSuite
    public void beforeSuiteObject3() {
        //does nothing
    }

    @BeforeTest
    public static void beforeTestStatic3() {
        //does nothing
    }

    @BeforeTest
    public void beforeTestObject3() {
        //does nothing
    }

    @BeforeClass
    public static void beforeClassStatic3() {
        //does nothing
    }

    @BeforeClass
    public void beforeClassObject3() {
        //does nothing
    }

    @BeforeMethod
    public static void beforeMethodStatic3() {
        //does nothing
    }

    @BeforeMethod
    public void beforeMethodObject3() {
        //does nothing
    }

    @Test
    public void someEmptyTest() {
       assertThat(true, is(true));
    }

    @Test
    public void someEmptyTest2() {
        assertThat(false, is(false));
    }

    @Test
    public void someEmptyTest3() {
        assertThat(true, is(true));
    }
}
