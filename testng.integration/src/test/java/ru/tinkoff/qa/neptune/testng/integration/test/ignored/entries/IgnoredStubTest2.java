package ru.tinkoff.qa.neptune.testng.integration.test.ignored.entries;

import ru.tinkoff.qa.neptune.testng.integration.test.BaseTestNgIntegrationTest;
import org.testng.annotations.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IgnoredStubTest2 extends BaseTestNgIntegrationTest {

    @BeforeSuite
    public static void beforeSuiteStatic4() {
        //does nothing
    }

    @BeforeSuite
    public void beforeSuiteObject4() {
        int i = 1;
    }

    @BeforeTest
    public static void beforeTestStatic4() {
        //does nothing
    }

    @BeforeTest
    public void beforeTestObject4() {
        //does nothing
    }

    @BeforeClass
    public static void beforeClassStatic4() {
        //does nothing
    }

    @BeforeClass
    public void beforeClassObject4() {
        int i = 0;
    }

    @BeforeMethod
    public static void beforeMethodStatic4() {
        //does nothing
    }

    @BeforeMethod
    public void beforeMethodObject4() {
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
