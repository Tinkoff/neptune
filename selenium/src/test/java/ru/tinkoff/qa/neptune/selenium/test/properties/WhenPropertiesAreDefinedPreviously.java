package ru.tinkoff.qa.neptune.selenium.test.properties;

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

import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.FIREFOX;
import static ru.tinkoff.qa.neptune.selenium.properties.FlagProperties.*;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.GENERAL_PROPERTIES;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.refreshProperties;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.WEB_DRIVER_TO_LAUNCH;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING_DURATION;
import static ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.FirefoxTestSupplierWithProfile.EMPTY_PROFILE;
import static java.lang.String.format;
import static java.time.Duration.*;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Map.entry;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.getFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.Platform.MAC;

/**
 * For the testing of the case if properties are partially defined before selenium.properties are read.
 */
@SuppressWarnings("unchecked")
public class WhenPropertiesAreDefinedPreviously {

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
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "HOURS");
        System.setProperty(WAITING_ALERT_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_WINDOW_TIME_UNIT.getPropertyName(), "DAYS");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "NANOS");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(WAITING_ALERT_TIME_VALUE.getPropertyName(), "45");
        System.setProperty(WAITING_WINDOW_TIME_VALUE.getPropertyName(), "1500");
        System.setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "100");

        System.setProperty(REMOTE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "https://github.com");

        System.setProperty(BROWSER_NAME.getPropertyName(), "safari");
        System.setProperty(PLATFORM_NAME.getPropertyName(), "Mac");
        System.setProperty(SUPPORTS_JAVASCRIPT.getPropertyName(), "true");

        System.setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "FALSE");
        System.setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "false");

        System.setProperty(WEB_DRIVER_TO_LAUNCH, "FIREFOX_DRIVER");
        System.setProperty(FIREFOX.getPropertyName(), "withProfile");

        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream(GENERAL_PROPERTIES)) {
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
                is(false));

        assertThat(format("Property %s", KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName()),
                KEEP_WEB_DRIVER_SESSION_OPENED.get(),
                is(false));
    }

    @Test
    public void testOfURLProperties() throws Exception {
        assertThat(format("Property %s", REMOTE_WEB_DRIVER_URL_PROPERTY.getPropertyName()),
                REMOTE_WEB_DRIVER_URL_PROPERTY.get(),
                is(new URL("https://github.com")));

        assertThat(format("Property %s", BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName()),
                BASE_WEB_DRIVER_URL_PROPERTY.get(),
                is(new URL("http://www.google.com")));
    }

    @Test
    public void testOfWaitingProperties() {
        assertThat(format("Property %s", ELEMENT_WAITING_TIME_UNIT.getPropertyName()),
                ELEMENT_WAITING_TIME_UNIT.get(),
                is(HOURS));

        assertThat(format("Property %s", WAITING_ALERT_TIME_UNIT.getPropertyName()),
                WAITING_ALERT_TIME_UNIT.get(),
                is(MINUTES));

        assertThat(format("Property %s", WAITING_WINDOW_TIME_UNIT.getPropertyName()),
                WAITING_WINDOW_TIME_UNIT.get(),
                is(DAYS));

        assertThat(format("Property %s", WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName()),
                WAITING_FRAME_SWITCHING_TIME_UNIT.get(),
                is(NANOS));

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


        assertThat("Web element waiting duration", ELEMENT_WAITING_DURATION.get(), is(ofHours(3)));
        assertThat("Alert waiting duration", WAITING_ALERT_TIME_DURATION.get(), is(ofMinutes(45)));
        assertThat("Window waiting duration", WAITING_WINDOW_TIME_DURATION.get(), is(ofDays(1500)));
        assertThat("Frame switching duration", WAITING_FRAME_SWITCHING_DURATION.get(), is(ofNanos(100)));
    }

    @Test
    public void testOfCommonCapabilityProperties() {
        assertThat(format("Property %s", BROWSER_NAME.getPropertyName()),
                BROWSER_NAME.get(),
                is(BrowserType.SAFARI));

        assertThat(format("Property %s", PLATFORM_NAME.getPropertyName()),
                PLATFORM_NAME.get(),
                is("Mac"));

        assertThat(format("Property %s", SUPPORTS_JAVASCRIPT.getPropertyName()),
                SUPPORTS_JAVASCRIPT.get(),
                is(true));

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
        assertThat("Platform info", capabilities, hasEntry(CapabilityType.PLATFORM_NAME, "Mac"));
        assertThat("Java script enabled info", capabilities, hasEntry(CapabilityType.SUPPORTS_JAVASCRIPT, true));
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
        assertThat("Platform info", firefoxOptions.getPlatform(), is(MAC));
        assertThat("Java script enabled info", firefoxOptions.getCapability("javascriptEnabled"),
                is( true));
        assertThat("Browser version info", firefoxOptions.getCapability("browserVersion"),
                is("60"));
        assertThat("Firefox profile", firefoxOptions.getProfile(), equalTo(EMPTY_PROFILE));
    }

    @AfterClass
    public void afterTests() throws Exception {
        properties.keySet().forEach(s -> System.getProperties().remove(s));
        File toDelete = getFile(GENERAL_PROPERTIES);
        if (toDelete.exists()) {
            forceDelete(toDelete);
        }
    }
}

