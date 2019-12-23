package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class TestNgInstantiationTest extends BaseTestNgIntegrationTest {

    @Test
    public void instantiationTest() {
        assertThat(ContextClass1.context, not(nullValue()));
        assertThat(ContextClass2.context, not(nullValue()));
        assertThat(ContextClass2.context.getA(), is(1));
        assertThat(ContextClass2.context.getB(), is(2));
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
        assertThat(ContextClass2.context.getA() + integer, is(1 + integer));
        assertThat(ContextClass2.context.getB() + integer, is(2 + integer));
    }
}
