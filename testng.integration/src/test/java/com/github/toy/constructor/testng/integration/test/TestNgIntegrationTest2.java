package com.github.toy.constructor.testng.integration.test;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestNgIntegrationTest2 extends BaseTestNgIntegrationTest {

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
