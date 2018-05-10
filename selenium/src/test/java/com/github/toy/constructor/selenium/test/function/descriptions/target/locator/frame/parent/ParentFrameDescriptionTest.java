package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.frame.parent;

import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.frame.parent.ParentFrameSupplier.parentFrame;
import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParentFrameDescriptionTest {

    @Test
    public void parentFrameDescriptionTest() {
        assertThat(parentFrame().get().toString(),
                is("Parent frame"));
    }

    @Test
    public void parentFrameWithTimeOutDescriptionTest() {
        assertThat(parentFrame(ofMillis(500)).get().toString(),
                is("Parent frame. Time to get valuable result: 0:00:00:500"));
    }
}
