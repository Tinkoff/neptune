package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.lang.Thread.currentThread;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(UserCustomExtension.class)
public class Junit5StubTest extends BaseJunit5IntegrationTest {

    @BeforeAll
    public static void beforeAllStatic2() {
        System.out.println("Static inheritor beforeAll in " + currentThread());
    }

    @BeforeAll
    public void beforeAllObject2() {
        System.out.println("Object inheritor beforeAll in " + currentThread());
    }

    @BeforeEach
    public void beforeEach2() {
        System.out.println("Object inheritor beforeEach in " + currentThread());
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
