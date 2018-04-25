package com.github.toy.constructor.selenium.test.properties;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.github.toy.constructor.selenium.properties.CapabilityTypes.CHROME;
import static com.github.toy.constructor.selenium.properties.CapabilityTypes.CommonCapabilityProperties.*;
import static com.github.toy.constructor.selenium.properties.CapabilityTypes.FIREFOX;
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
import static java.util.Map.entry;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.getFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.Platform.LINUX;

/**
 * For the testing of the case if all properties are defined in selenium.properties.
 */
public class DefaultPropertiesTest {

    private static final Map<String, String> properties = Map
            .ofEntries(entry(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES"),
                    entry(WAITING_ALERT_TIME_UNIT.getPropertyName(), "SECONDS"),
                    entry(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "MILLIS"),
                    entry(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS"),
                    entry(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3"),
                    entry(WAITING_ALERT_TIME_VALUE.getPropertyName(), "45"),
                    entry(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "1500"),
                    entry(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "100"),
                    entry(REMOTE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "https://www.youtube.com"),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "http://www.google.com"),
                    entry(BROWSER_NAME.getPropertyName(), "firefox"),
                    entry(PLATFORM_NAME.getPropertyName(), "Linux"),
                    entry(SUPPORTS_JAVASCRIPT.getPropertyName(), "false"),
                    entry(BROWSER_VERSION.getPropertyName(), "60"),
                    entry(CHROME.getPropertyName(), "withExperimental"),
                    entry(CLEAR_WEB_DRIVER_COOKIES.getPropertyName(), "true"),
                    entry(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true"),
                    entry(GET_BACK_TO_BASE_URL.getPropertyName(), "true"),
                    entry(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "true"),
                    entry(WEB_DRIVER_TO_LAUNCH, "CHROME_DRIVER"));

    @BeforeClass
    public void beforeTests() throws Exception {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream(SELENIUM_PROPERTY_FILE)) {
            // set the properties value
            properties.forEach(prop::setProperty);
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

    @Test
    public void testOfCommonCapabilityProperties() {
        assertThat(format("Property %s", BROWSER_NAME.getPropertyName()),
                BROWSER_NAME.get(),
                is(BrowserType.FIREFOX));

        assertThat(format("Property %s", PLATFORM_NAME.getPropertyName()),
                PLATFORM_NAME.get(),
                is("Linux"));

        assertThat(format("Property %s", SUPPORTS_JAVASCRIPT.getPropertyName()),
                SUPPORTS_JAVASCRIPT.get(),
                is(false));

        assertThat(format("Property %s", BROWSER_VERSION.getPropertyName()),
                BROWSER_VERSION.get(),
                is("60"));
    }

    @Test
    public void testOfSuppliedCapabilityProperties() {
        ChromeOptions capabilitiesAsIs = (ChromeOptions) CHROME.get();
        Map<String, ?> capabilities = capabilitiesAsIs.asMap();
        assertThat("Result map size", capabilities.size(), is(5));
        //IsMapContaining
        assertThat("Browser info", capabilities, hasEntry(CapabilityType.BROWSER_NAME, BrowserType.CHROME));
        assertThat("Platform info", capabilities, hasEntry(CapabilityType.PLATFORM_NAME, "Linux"));
        assertThat("Java script enabled info", capabilities, hasEntry(CapabilityType.SUPPORTS_JAVASCRIPT, false));
        assertThat("Browser version info", capabilities, hasEntry("browserVersion", "60"));
        assertThat("Chrome options info", capabilities, hasKey("goog:chromeOptions"));

        Map<String, ?> args = (Map<String, ?>) capabilitiesAsIs.getCapability("goog:chromeOptions");
        assertThat("arguments", (List<String>) args.get("args"), contains("--use-fake-device-for-media-stream",
                "--start-maximized",
                "--enable-automation",
                "--disable-web-security"));

        assertThat("arguments", args.get("experimentalOption"), equalTo(new HashMap<>()));

        FirefoxOptions firefoxOptions = (FirefoxOptions) FIREFOX.get();
        assertThat("Browser info", firefoxOptions.getBrowserName(), is(BrowserType.FIREFOX));
        assertThat("Platform info", firefoxOptions.getPlatform(), is(LINUX));
        assertThat("Java script enabled info", firefoxOptions.getCapability("javascriptEnabled"),
                is( false));
        assertThat("Browser version info", firefoxOptions.getCapability("browserVersion"),
                is("60"));
        assertThat("Firefox profile", firefoxOptions.getProfile(), nullValue());
    }

    @AfterClass
    public void afterTests() throws Exception {
        properties.keySet().forEach(s -> System.getProperties().remove(s));
        File toDelete = getFile(SELENIUM_PROPERTY_FILE);
        if (toDelete.exists()) {
            forceDelete(toDelete);
        }
    }
}
