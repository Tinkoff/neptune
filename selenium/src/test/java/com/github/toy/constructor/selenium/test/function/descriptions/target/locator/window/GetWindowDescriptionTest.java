package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.window;

import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasTitle;
import static com.github.toy.constructor.selenium.functions.target.locator.window.WindowPredicates.hasUrl;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_WINDOW_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_WINDOW_TIME_VALUE;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetWindowDescriptionTest {

    @Test
    public void withoutParametersTest() {
        assertThat(window().toString(), is("The first window/tab"));
    }

    @Test
    public void withIndexParameterTest() {
        assertThat(window().byIndex(4).toString(),
                is("Window/tab by index 4. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void withIndexParameterAndTimeDefinedImplicitlyTest() {
        System.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(window().byIndex(4).toString(),
                    is("Window/tab by index 4. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_WINDOW_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_WINDOW_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void withConditionParameterTest() {
        assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url"))).toString(),
                is("Window/tab with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void withConditionParameterAndTimeDefinedImplicitlyTest() {
        System.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url"))).toString(),
                    is("Window/tab with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_WINDOW_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_WINDOW_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void withDurationParameterTest() {
        assertThat(window().withTimeToGetWindow(ofSeconds(7)).toString(),
                is("The first window/tab"));
    }

    @Test
    public void withDurationAndIndexParameterTest() {
        assertThat(window().byIndex(4).withTimeToGetWindow(ofSeconds(7)).toString(),
                is("Window/tab by index 4. Time to get valuable result: 0:00:07:000"));
    }

    @Test
    public void withDurationAndIndexParameterAndTimeDefinedImplicitlyTest() {
        System.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(window().byIndex(4).withTimeToGetWindow(ofSeconds(7)).toString(),
                    is("Window/tab by index 4. Time to get valuable result: 0:00:07:000"));
        }
        finally {
            System.getProperties().remove(WAITING_WINDOW_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_WINDOW_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void withDurationAndConditionParameterTest() {
        assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url")))
                        .withTimeToGetWindow(ofSeconds(7)).toString(),
                is("Window/tab with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                        "Time to get valuable result: 0:00:07:000"));
    }

    @Test
    public void withDurationAndConditionParameterAndTimeDefinedImplicitlyTest() {
        System.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url")))
                            .withTimeToGetWindow(ofSeconds(7)).toString(),
                    is("Window/tab with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                            "Time to get valuable result: 0:00:07:000"));
        }
        finally {
            System.getProperties().remove(WAITING_WINDOW_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_WINDOW_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void withFullParameterTest() {
        assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url")))
                        .withTimeToGetWindow(ofSeconds(7)).byIndex(4).toString(),
                is("Window/tab by index 4 with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                        "Time to get valuable result: 0:00:07:000"));
    }

    @Test
    public void withFullParameterAndTimeDefinedImplicitlyTest() {
        System.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url")))
                            .withTimeToGetWindow(ofSeconds(7)).byIndex(4).toString(),
                    is("Window/tab by index 4 with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                            "Time to get valuable result: 0:00:07:000"));
        }
        finally {
            System.getProperties().remove(WAITING_WINDOW_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_WINDOW_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void withIndexAndConditionTest() {
        assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url")))
                        .byIndex(4).toString(),
                is("Window/tab by index 4 with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void withIndexAndConditionAndTimeDefinedImplicitlyTest() {
        System.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(window().onCondition(hasTitle("Some title").and(hasUrl("some url")))
                            .byIndex(4).toString(),
                    is("Window/tab by index 4 with condition (Has title 'Some title') AND (Has loaded url 'some url'). " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_WINDOW_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_WINDOW_TIME_VALUE.getPropertyName());
        }
    }
}
