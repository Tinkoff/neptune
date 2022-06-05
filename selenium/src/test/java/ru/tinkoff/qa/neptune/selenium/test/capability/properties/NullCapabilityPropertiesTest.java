package ru.tinkoff.qa.neptune.selenium.test.capability.properties;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.Test;

import java.util.Map;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.Platform.LINUX;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.FIREFOX;

/**
 * For the testing of the case if all properties are not defined in any way.
 */
public class NullCapabilityPropertiesTest {

    @Test
    public void testOfCommonCapabilityProperties() {
        assertThat(format("Property %s", BROWSER_NAME.getName()),
                BROWSER_NAME.get(),
                nullValue());

        assertThat(format("Property %s", PLATFORM_NAME.getName()),
                PLATFORM_NAME.get(),
                nullValue());

        assertThat(format("Property %s", BROWSER_VERSION.getName()),
                BROWSER_VERSION.get(),
                nullValue());
    }

    @Test
    public void testOfSuppliedCapabilityProperties() {
        ChromeOptions capabilitiesAsIs = (ChromeOptions) CHROME.get();
        Map<String, ?> capabilities = capabilitiesAsIs.asMap();
        assertThat("Result map size", capabilities.size(), is(2));
        //IsMapContaining
        assertThat("Browser info", capabilities, hasEntry(CapabilityType.BROWSER_NAME, BrowserType.CHROME));
        assertThat("Platform info", capabilities, not(hasEntry(CapabilityType.PLATFORM_NAME, "LINUX")));
        assertThat("Browser version info", capabilities, not(hasEntry("browserVersion", "60")));
        assertThat("Chrome options info", capabilities, hasKey("goog:chromeOptions"));

        assertThat("arguments", capabilitiesAsIs.getCapability("goog:chromeOptions"), notNullValue());

        FirefoxOptions firefoxOptions = (FirefoxOptions) FIREFOX.get();
        assertThat("Browser info", firefoxOptions.getBrowserName(), is(BrowserType.FIREFOX));
        assertThat("Platform info", firefoxOptions.getPlatform(), not(is(LINUX)));
        assertThat("Browser version info", firefoxOptions.getCapability("browserVersion"),
                not(is("60")));
        assertThat("Firefox profile", firefoxOptions.getProfile(), notNullValue());
    }
}
