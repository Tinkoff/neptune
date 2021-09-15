package ru.tinkoff.qa.neptune.jupiter.integration;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@Tag("Junit5")
public class DependencyInjectionTest extends BaseJunit5Test {

    private int a;
    private int b;
    private int c;
    private Integer d;

    @Test
    public void test() {
        assertThat("a", a, is(1));
        assertThat("b", b, is(2));
        assertThat("c", c, is(3));
        assertThat("d", d, nullValue());
    }
}
