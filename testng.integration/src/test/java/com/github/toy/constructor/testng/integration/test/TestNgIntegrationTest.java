package com.github.toy.constructor.testng.integration.test;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class TestNgIntegrationTest extends BaseTestNgIntegrationTest {

    @Test
    public void instantiationTest() {
        assertThat(getStepClass1(), not(nullValue()));
        assertThat(getStepClass2(), not(nullValue()));
        assertThat(getStepClass2().getA(), is(1));
        assertThat(getStepClass2().getB(), is(2));
        assertThat(getStepClass3(), nullValue());
        assertThat(getStepClass4(), nullValue());
    }
}
