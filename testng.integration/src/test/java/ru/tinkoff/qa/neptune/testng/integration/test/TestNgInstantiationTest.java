package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class TestNgInstantiationTest extends BaseTestNgIntegrationTest {

    static TestNgInstantiationTest testNgInstantiationTest;

    public TestNgInstantiationTest() {
        testNgInstantiationTest = this;
    }

    @Test
    public void instantiationTest() {
        assertThat(getStepClass1(), not(nullValue()));
        assertThat(getStepClass2(), not(nullValue()));
        assertThat(getStepClass2().getA(), is(1));
        assertThat(getStepClass2().getB(), is(2));
        assertThat(getStepClass3(), nullValue());
        assertThat(getStepClass4(), nullValue());
    }

    @DataProvider(parallel = true)
    public Object[][] dataProvider() {
        return new Object[][]{
                {1},
                {2},
                {3},
                {4},
                {5},
        };
    }

    @Test(dataProvider = "dataProvider")
    public void instantiationTest2(Integer integer) {
        assertThat(getStepClass2().getA() + integer, is(1 + integer));
        assertThat(getStepClass2().getB() + integer, is(2 + integer));
    }
}
