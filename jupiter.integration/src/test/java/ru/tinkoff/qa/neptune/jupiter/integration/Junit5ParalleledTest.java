package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Tag("Junit5")
@Execution(CONCURRENT)
public class Junit5ParalleledTest extends BaseJunit5IntegrationTest {

    @Test
    public void instantiationTest() {
        assertThat(ContextClass1.context, not(nullValue()));
        assertThat(ContextClass2.context, not(nullValue()));
        assertThat(ContextClass2.context.getA(), is(1));
        assertThat(ContextClass2.context.getB(), is(2));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void instantiationTest2(int integer) {
        assertThat(ContextClass2.context.getA() + integer, is(1 + integer));
        assertThat(ContextClass2.context.getB() + integer, is(2 + integer));
    }
}
