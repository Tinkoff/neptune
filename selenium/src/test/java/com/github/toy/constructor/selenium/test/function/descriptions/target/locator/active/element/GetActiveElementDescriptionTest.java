package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.active.element;

import org.testng.annotations.Test;

import java.time.Duration;

import static com.github.toy.constructor.selenium.functions.target.locator.active.element.GetActiveElementSupplier.activeElement;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetActiveElementDescriptionTest {

    @Test
    public void activeElementWithoutTimeOut() {
        assertThat(activeElement().get().toString(), is("Active element on condition as is"));
    }

    @Test
    public void activeElementWithTimeOut() {
        assertThat(activeElement(ofSeconds(30)).get().toString(), is("Active element on condition as is. " +
                "Time to get valuable result: 0:00:30:000"));
    }
}
