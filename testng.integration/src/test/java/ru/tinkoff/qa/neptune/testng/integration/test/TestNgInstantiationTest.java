package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;

public class TestNgInstantiationTest extends BaseTestNgIntegrationTest {

    @Test
    public void instantiationTest() {
        assertThat(getCreatedContextOrCreate(ContextClass1.class), not(nullValue()));
        assertThat(getCreatedContextOrCreate(ContextClass2.class), not(nullValue()));
        assertThat(getCreatedContextOrCreate(ContextClass2.class).getA(), is(1));
        assertThat(getCreatedContextOrCreate(ContextClass2.class).getB(), is(2));
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
        assertThat(getCreatedContextOrCreate(ContextClass2.class).getA() + integer, is(1 + integer));
        assertThat(getCreatedContextOrCreate(ContextClass2.class).getB() + integer, is(2 + integer));
    }
}
