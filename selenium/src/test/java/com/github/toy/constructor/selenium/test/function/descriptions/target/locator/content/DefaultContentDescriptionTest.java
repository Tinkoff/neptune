package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.content;

import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;
import static java.time.Duration.ofMinutes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DefaultContentDescriptionTest {

    @Test
    public void defaultContentDescriptionTest() {
        assertThat(defaultContent().get().toString(),
                is("Default content on condition as is"));
    }

    @Test
    public void defaultContentWithTimeOutDescriptionTest() {
        assertThat(defaultContent(ofMinutes(5)).get().toString(),
                is("Default content on condition as is. Time to get valuable result: 0:05:00:000"));
    }
}
