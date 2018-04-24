package com.github.toy.constructor.selenium.test.properties;


import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import static com.github.toy.constructor.selenium.properties.CapabilityTypes.CHROME;
import static com.github.toy.constructor.selenium.properties.CapabilityTypes.CommonCapabilityProperties.*;
import static com.github.toy.constructor.selenium.properties.FlagProperties.*;
import static com.github.toy.constructor.selenium.properties.SeleniumPropertyInitializer.SELENIUM_PROPERTY_FILE;
import static com.github.toy.constructor.selenium.properties.SeleniumPropertyInitializer.refreshProperties;
import static com.github.toy.constructor.selenium.properties.SupportedWebDriverPropertyProperty.WEB_DRIVER_TO_LAUNCH;
import static com.github.toy.constructor.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static com.github.toy.constructor.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.*;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.*;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.*;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.getFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DefaultPropertiesTest {

    @BeforeClass
    public void beforeTests() throws Exception {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream(SELENIUM_PROPERTY_FILE)) {

            // set the properties value
            prop.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
            prop.setProperty(WAITING_ALERT_TIME_UNIT.getPropertyName(), "SECONDS");
            prop.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MILLIS");
            prop.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
            prop.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
            prop.setProperty(WAITING_ALERT_TIME_VALUE.getPropertyName(), "45");
            prop.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "1500");
            prop.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "100");
            prop.setProperty(REMOTE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "https://www.youtube.com");
            prop.setProperty(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "http://www.google.com");
            prop.setProperty(BROWSER_NAME.getPropertyName(), "firefox");
            prop.setProperty(PLATFORM_NAME.getPropertyName(), "Linux");
            prop.setProperty(SUPPORTS_JAVASCRIPT.getPropertyName(), "false");
            prop.setProperty(BROWSER_VERSION.getPropertyName(), "60");
            prop.setProperty(WEB_DRIVER_TO_LAUNCH, "CHROME_DRIVER");
            prop.setProperty(CHROME.getPropertyName(), "withArguments,withArguments2");
            prop.setProperty(CLEAR_WEB_DRIVER_COOKIES.getPropertyName(), "true");
            prop.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");
            prop.setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "true");
            prop.setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "true");

            // save properties to project root folder
            prop.store(output, null);
            refreshProperties();
        }
    }

    @Test
    public void testOfFlagProperties() {
        assertThat(format("Property %s", CLEAR_WEB_DRIVER_COOKIES.getPropertyName()),
                CLEAR_WEB_DRIVER_COOKIES.get(),
                is(true));

        assertThat(format("Property %s", FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName()),
                FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.get(),
                is(true));

        assertThat(format("Property %s", GET_BACK_TO_BASE_URL.getPropertyName()),
                GET_BACK_TO_BASE_URL.get(),
                is(true));

        assertThat(format("Property %s", KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName()),
                KEEP_WEB_DRIVER_SESSION_OPENED.get(),
                is(true));
    }

    @Test
    public void testOfURLProperties() throws Exception {
        assertThat(format("Property %s", REMOTE_WEB_DRIVER_URL_PROPERTY.getPropertyName()),
                REMOTE_WEB_DRIVER_URL_PROPERTY.get(),
                is(new URL("https://www.youtube.com")));

        assertThat(format("Property %s", BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName()),
                BASE_WEB_DRIVER_URL_PROPERTY.get(),
                is(new URL("http://www.google.com")));
    }

    @Test
    public void testOfWaitingProperties() {
        assertThat(format("Property %s", ELEMENT_WAITING_TIME_UNIT.getPropertyName()),
                ELEMENT_WAITING_TIME_UNIT.get(),
                is(MINUTES));

        assertThat(format("Property %s", WAITING_ALERT_TIME_UNIT.getPropertyName()),
                WAITING_ALERT_TIME_UNIT.get(),
                is(SECONDS));

        assertThat(format("Property %s", WAITING_WINDOW_TIME_UNIT.getPropertyName()),
                WAITING_WINDOW_TIME_UNIT.get(),
                is(MILLIS));

        assertThat(format("Property %s", WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName()),
                WAITING_FRAME_SWITCHING_TIME_UNIT.get(),
                is(SECONDS));

        assertThat(format("Property %s", ELEMENT_WAITING_TIME_VALUE.getPropertyName()),
                ELEMENT_WAITING_TIME_VALUE.get(),
                is(3L));

        assertThat(format("Property %s", WAITING_ALERT_TIME_VALUE.getPropertyName()),
                WAITING_ALERT_TIME_VALUE.get(),
                is(45L));

        assertThat(format("Property %s", WAITING_WINDOW_TIME_VALUE.getPropertyName()),
                WAITING_WINDOW_TIME_VALUE.get(),
                is(1500L));

        assertThat(format("Property %s", WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName()),
                WAITING_FRAME_SWITCHING_TIME_VALUE.get(),
                is(100L));


        assertThat(format("Duration item %s", ELEMENT_WAITING_DURATION.name()),
                ELEMENT_WAITING_DURATION.get(),
                is(ofMinutes(3)));

        assertThat(format("Duration item %s", WAITING_ALERT_TIME_DURATION.name()),
                WAITING_ALERT_TIME_DURATION.get(),
                is(ofSeconds(45)));

        assertThat(format("Duration item %s", WAITING_WINDOW_TIME_DURATION.name()),
                WAITING_WINDOW_TIME_DURATION.get(),
                is(ofMillis(1500)));

        assertThat(format("Duration item %s", WAITING_FRAME_SWITCHING_DURATION.name()),
                WAITING_FRAME_SWITCHING_DURATION.get(),
                is(ofSeconds(100)));
    }

    @AfterTest
    public void afterTests() throws Exception {
        File toDelete = getFile(SELENIUM_PROPERTY_FILE);
        if (toDelete.exists()) {
            forceDelete(toDelete);
        }
    }
}
