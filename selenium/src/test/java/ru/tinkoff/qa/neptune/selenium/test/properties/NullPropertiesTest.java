package ru.tinkoff.qa.neptune.selenium.test.properties;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.Test;

import java.util.Map;

import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.BROWSER_VERSION;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.FIREFOX;
import static ru.tinkoff.qa.neptune.selenium.properties.FlagProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.FlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FRAME_SWITCHING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FRAME_SWITCHING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING_DURATION;
import static java.lang.String.format;
import static java.time.Duration.ofMinutes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.Platform.LINUX;

/**
 * For the testing of the case if all properties are not defined in any way.
 */
public class NullPropertiesTest {

    @Test
    public void testOfFlagProperties() {
        assertThat(format("Property %s", CLEAR_WEB_DRIVER_COOKIES.getPropertyName()),
                CLEAR_WEB_DRIVER_COOKIES.get(),
                is(false));

        assertThat(format("Property %s", FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName()),
                FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.get(),
                is(false));

        assertThat(format("Property %s", GET_BACK_TO_BASE_URL.getPropertyName()),
                GET_BACK_TO_BASE_URL.get(),
                is(false));

        assertThat(format("Property %s", KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName()),
                KEEP_WEB_DRIVER_SESSION_OPENED.get(),
                is(false));
    }

    @Test
    public void testOfURLProperties() {
        assertThat(format("Property %s", REMOTE_WEB_DRIVER_URL_PROPERTY.getPropertyName()),
                REMOTE_WEB_DRIVER_URL_PROPERTY.get(),
                nullValue());

        assertThat(format("Property %s", BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName()),
                BASE_WEB_DRIVER_URL_PROPERTY.get(),
                nullValue());
    }

    @Test
    public void testOfWaitingProperties() {
        assertThat(format("Property %s", ELEMENT_WAITING_TIME_UNIT.getPropertyName()),
                ELEMENT_WAITING_TIME_UNIT.get(),
                nullValue());

        assertThat(format("Property %s", WAITING_ALERT_TIME_UNIT.getPropertyName()),
                WAITING_ALERT_TIME_UNIT.get(),
                nullValue());

        assertThat(format("Property %s", WAITING_WINDOW_TIME_UNIT.getPropertyName()),
                WAITING_WINDOW_TIME_UNIT.get(),
                nullValue());

        assertThat(format("Property %s", WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName()),
                WAITING_FRAME_SWITCHING_TIME_UNIT.get(),
                nullValue());

        assertThat(format("Property %s", ELEMENT_WAITING_TIME_VALUE.getPropertyName()),
                ELEMENT_WAITING_TIME_VALUE.get(),
                nullValue());

        assertThat(format("Property %s", WAITING_ALERT_TIME_VALUE.getPropertyName()),
                WAITING_ALERT_TIME_VALUE.get(),
                nullValue());

        assertThat(format("Property %s", WAITING_WINDOW_TIME_VALUE.getPropertyName()),
                WAITING_WINDOW_TIME_VALUE.get(),
                nullValue());

        assertThat(format("Property %s", WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName()),
                WAITING_FRAME_SWITCHING_TIME_VALUE.get(),
                nullValue());


        assertThat("Web element waiting duration", ELEMENT_WAITING_DURATION.get(), is(ofMinutes(1)));
        assertThat("Alert waiting duration", WAITING_ALERT_TIME_DURATION.get(), is(ofMinutes(1)));
        assertThat("Window waiting duration", WAITING_WINDOW_TIME_DURATION.get(), is(ofMinutes(1)));
        assertThat("Frame switching duration", WAITING_FRAME_SWITCHING_DURATION.get(), is(ofMinutes(1)));
    }

    @Test
    public void testOfCommonCapabilityProperties() {
        assertThat(format("Property %s", BROWSER_NAME.getPropertyName()),
                BROWSER_NAME.get(),
                nullValue());

        assertThat(format("Property %s", PLATFORM_NAME.getPropertyName()),
                PLATFORM_NAME.get(),
                nullValue());

        assertThat(format("Property %s", SUPPORTS_JAVASCRIPT.getPropertyName()),
                SUPPORTS_JAVASCRIPT.get(),
                is(true));

        assertThat(format("Property %s", BROWSER_VERSION.getPropertyName()),
                BROWSER_VERSION.get(),
                nullValue());
    }

    @Test
    public void testOfSuppliedCapabilityProperties() {
        ChromeOptions capabilitiesAsIs = (ChromeOptions) CHROME.get();
        Map<String, ?> capabilities = capabilitiesAsIs.asMap();
        assertThat("Result map size", capabilities.size(), is(3));
        //IsMapContaining
        assertThat("Browser info", capabilities, hasEntry(CapabilityType.BROWSER_NAME, BrowserType.CHROME));
        assertThat("Platform info", capabilities, not(hasEntry(CapabilityType.PLATFORM_NAME, "Linux")));
        assertThat("Java script enabled info", capabilities, hasEntry(CapabilityType.SUPPORTS_JAVASCRIPT, true));
        assertThat("Browser version info", capabilities, not(hasEntry("browserVersion", "60")));
        assertThat("Chrome options info", capabilities, hasKey("goog:chromeOptions"));

        assertThat("arguments", capabilitiesAsIs.getCapability("goog:chromeOptions"), nullValue());

        FirefoxOptions firefoxOptions = (FirefoxOptions) FIREFOX.get();
        assertThat("Browser info", firefoxOptions.getBrowserName(), is(BrowserType.FIREFOX));
        assertThat("Platform info", firefoxOptions.getPlatform(), not(is(LINUX)));
        assertThat("Java script enabled info", firefoxOptions.getCapability("javascriptEnabled"),
                is( true));
        assertThat("Browser version info", firefoxOptions.getCapability("browserVersion"),
                not(is("60")));
        assertThat("Firefox profile", firefoxOptions.getProfile(), nullValue());
    }
}
