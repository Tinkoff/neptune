package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

/**
 * This is the integration sanity test which is supposed to be run on some local environment.
 * Goals:
 * to make sure that {@link SeleniumParameterProvider} works as expected and returns {@link WrappedWebDriver}
 * to make sure that {@link WrappedWebDriver} can at least open and close desired browser
 * <p>
 * Requirements:
 * Installed Chrome
 */
public class SanityTestOfTheStartingAndStoppingOfWebDriver {

    private static boolean isDriverAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Test()
    public void testOfTheStarting() {
        Map<String, String> properties = new HashMap<>(ofEntries(
                entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                entry(CHROME.getName(), ChromeSettingsSupplierHeadless.class.getName())));
        properties.forEach(System::setProperty);

        try {
            WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                    new SeleniumParameterProvider().provide()[0]);
            WebDriver driver = null;
            try {
                driver = wrappedWebDriver.getWrappedDriver();
                assertThat("Check class of web driver", ChromeDriver.class.isAssignableFrom(driver.getClass()),
                        is(true));
                assertThat("Web driver is alive", isDriverAlive(driver), is(true));
                assertThat("Current url", driver.getCurrentUrl(), anyOf(is("about:blank"), is("data:,")));
            } finally {
                wrappedWebDriver.shutDown();
                ofNullable(driver).ifPresent(webDriver ->
                        assertThat("Web driver is dead", !isDriverAlive(webDriver), is(true)));
            }
        } finally {
            properties.keySet().forEach(s -> System.getProperties().remove(s));
        }
    }

    @Test
    public void startSessionWithBaseURL() {
        Map<String, String> properties = new HashMap<>(ofEntries(
                entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                entry(CHROME.getName(), ChromeSettingsSupplierHeadless.class.getName()),
                entry(BASE_WEB_DRIVER_URL_PROPERTY.getName(), "https://github.com/")));
        properties.forEach(System::setProperty);

        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide()[0]);
        try {
            assertThat("Current url",
                    wrappedWebDriver.getWrappedDriver().getCurrentUrl(),
                    is("https://github.com/"));
        } finally {
            wrappedWebDriver.shutDown();
            properties.keySet().forEach(s -> System.getProperties().remove(s));
        }
    }
}
