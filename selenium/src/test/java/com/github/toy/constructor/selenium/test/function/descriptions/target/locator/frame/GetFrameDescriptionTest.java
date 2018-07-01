package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.frame;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.test.function.descriptions.DescribedWebElement;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;


import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameFunction.*;
import static com.github.toy.constructor.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FRAME_SWITCHING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FRAME_SWITCHING_TIME_VALUE;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetFrameDescriptionTest {

    @Test
    public void indexWithTimeOutDescriptionTest() {
        assertThat(frame(index(ofSeconds(7), 2)).get().toString(),
                is("Frame by index 2. Time to get valuable result: 0:00:07:000"));
    }

    @Test
    public void indexWithoutTimeOutDescriptionTest() {
        assertThat(frame(index(2)).get().toString(),
                is("Frame by index 2. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void indexWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(index(2)).get().toString(),
                    is("Frame by index 2. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void nameOrIdWithTimeOutDescriptionTest() {
        assertThat(frame(nameOrId(ofSeconds(7), "someFrameName")).get().toString(),
                is("Frame by name or id someFrameName. Time to get valuable result: 0:00:07:000"));
    }

    @Test
    public void nameOrIdWithoutTimeOutDescriptionTest() {
        assertThat(frame(nameOrId("someFrameName")).get().toString(),
                is("Frame by name or id someFrameName. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void nameOrIdWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(nameOrId("someFrameName")).get().toString(),
                    is("Frame by name or id someFrameName. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void webElementWithTimeOutDescriptionTest() {
        assertThat(frame(insideElement(ofSeconds(7), new DescribedWebElement())).get().toString(),
                is("Frame inside element Test web element. Time to get valuable result: 0:00:07:000"));
    }

    @Test
    public void webElementWithoutTimeOutDescriptionTest() {
        assertThat(frame(insideElement(new DescribedWebElement())).get().toString(),
                is("Frame inside element Test web element. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void webElementWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(insideElement(new DescribedWebElement())).get().toString(),
                    is("Frame inside element Test web element. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void wrappedWebElementWithTimeOutDescriptionTest() {
        assertThat(frame(wrappedBy(ofSeconds(7), new FrameWidget(new DescribedWebElement()))).get().toString(),
                is("Frame inside element wrapped by Frame widget. " +
                        "Time to get valuable result: 0:00:07:000"));
    }


    @Test
    public void wrappedWebElementWithoutTimeOutDescriptionTest() {
        assertThat(frame(wrappedBy(new FrameWidget(new DescribedWebElement()))).get().toString(),
                is("Frame inside element wrapped by Frame widget. " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void wrappedWebElementWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(frame(wrappedBy(new FrameWidget(new DescribedWebElement()))).get().toString(),
                    is("Frame inside element wrapped by Frame widget. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Name("Frame widget")
    private static class FrameWidget extends Widget {

        FrameWidget(WebElement wrappedElement) {
            super(wrappedElement);
        }

        @Override
        public String toString() {
            return "Frame widget";
        }
    }
}
