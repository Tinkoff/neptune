package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(UserCustomExtension.class)
public class Junit5StubTest extends BaseJunit5IntegrationTest {

    @BeforeAll
    public static void beforeAllStatic2() {
        //does nothing
    }

    @Test
    public static void staticTest() {
        assertThat(true, is(true));
    }

    @BeforeAll
    public void beforeAllObject2() {
        //does nothing
    }

    @BeforeEach
    public void beforeEach2() {
        //does nothing
    }

    @Disabled
    @Test
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

    @Disabled
    @Test
    public void ignoredTest2() {
        assertThat(true, is(true));
    }
}
