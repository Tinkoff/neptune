package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.frame;

import com.github.toy.constructor.selenium.functions.searching.SearchSupplier;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWebElement;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameFunction.index;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameFunction.insideElement;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameFunction.nameOrId;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_ALERT_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FRAME_SWITCHING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_ALERT_TIME_VALUE;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FRAME_SWITCHING_TIME_VALUE;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.By.xpath;

public class GetFrameDescriptionTest {

    @Test
    public void indexWithTimeOutDescriptionTest() {
        assertThat(frame(index(ofSeconds(7), 2)).get().toString(),
                is("Frame by index 2. Time to get valuable result: 0:00:07:000 from (Current content)"));
    }

    @Test
    public void indexWithoutTimeOutDescriptionTest() {
        assertThat(frame(index(2)).get().toString(),
                is("Frame by index 2. Time to get valuable result: 0:01:00:000 from (Current content)"));
    }

    @Test
    public void indexWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(index(2)).get().toString(),
                    is("Frame by index 2. Time to get valuable result: 0:03:00:000 from (Current content)"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void nameOrIdWithTimeOutDescriptionTest() {
        assertThat(frame(nameOrId(ofSeconds(7), "someFrameName")).get().toString(),
                is("Frame by name or id someFrameName. " +
                        "Time to get valuable result: 0:00:07:000 from (Current content)"));
    }

    @Test
    public void nameOrIdWithoutTimeOutDescriptionTest() {
        assertThat(frame(nameOrId("someFrameName")).get().toString(),
                is("Frame by name or id someFrameName. " +
                        "Time to get valuable result: 0:01:00:000 from (Current content)"));
    }

    @Test
    public void nameOrIdWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(nameOrId("someFrameName")).get().toString(),
                    is("Frame by name or id someFrameName. " +
                            "Time to get valuable result: 0:03:00:000 from (Current content)"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void webElementWithTimeOutDescriptionTest() {
        assertThat(frame(insideElement(ofSeconds(7), new DescribedWebElement())).get().toString(),
                is("Frame inside element Test web element. " +
                        "Time to get valuable result: 0:00:07:000 from (Current content)"));
    }

    @Test
    public void webElementWithoutTimeOutDescriptionTest() {
        assertThat(frame(insideElement(new DescribedWebElement())).get().toString(),
                is("Frame inside element Test web element. " +
                        "Time to get valuable result: 0:01:00:000 from (Current content)"));
    }

    @Test
    public void webElementWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(insideElement(new DescribedWebElement())).get().toString(),
                    is("Frame inside element Test web element. " +
                            "Time to get valuable result: 0:03:00:000 from (Current content)"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void webElementSearchingWithTimeOutDescriptionTest() {
        assertThat(frame(insideElement(ofSeconds(7), webElement(xpath(".//some//path")))).get().toString(),
                is("Frame inside A single item from (Web elements located By.xpath: .//some//path). " +
                        "Time to get valuable result: 0:01:00:000. " +
                        "Time to get valuable result: 0:00:07:000 from (Current content)"));
    }


    @Test
    public void webElementSearchingWithoutTimeOutDescriptionTest() {
        assertThat(frame(insideElement(webElement(xpath(".//some//path")))).get().toString(),
                is("Frame inside A single item from (Web elements located By.xpath: .//some//path). " +
                        "Time to get valuable result: 0:01:00:000. " +
                        "Time to get valuable result: 0:01:00:000 from (Current content)"));
    }

    @Test
    public void webElementSearchingWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(insideElement(webElement(xpath(".//some//path")))).get().toString(),
                    is("Frame inside A single item from (Web elements located By.xpath: .//some//path). Time to get valuable result: 0:01:00:000. " +
                            "Time to get valuable result: 0:03:00:000 from (Current content)"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }
}
