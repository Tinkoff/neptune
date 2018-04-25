package com.github.toy.constructor.selenium.test.webdriver.starting;

import com.github.toy.constructor.selenium.SeleniumParameterProvider;
import com.github.toy.constructor.selenium.WrappedWebDriver;
import com.github.toy.constructor.selenium.properties.SupportedWebDrivers;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.github.toy.constructor.selenium.properties.SupportedWebDriverPropertyProperty.WEB_DRIVER_TO_LAUNCH;
import static com.github.toy.constructor.selenium.properties.SupportedWebDrivers.*;
import static java.util.Map.entry;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.Platform.*;

/**
 * This is the integration sanity test which is supposed to be run on some local environment.
 * Goals:
 * to make sure that {@link SeleniumParameterProvider} works as expected and returns {@link WrappedWebDriver}
 * to make sure that {@link WrappedWebDriver} can at least open and close desired browser
 *
 * Requirements:
 * Installed Chrome
 * Installed FireFix
 * Installed Safari if test is run on Mac Os X
 * Installed Edge if test is run on Windows 10
 * Installed Internet explorer if test is run on Windows 8
 */
public class SanityTestOfTheStartingAndStoppingOfWebDriver {

    private static boolean isDriverAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        }
        catch (WebDriverException e) {
            return false;
        }
    }

    private static Map.Entry<String, String> desiredDriver(SupportedWebDrivers supportedWebDriver) {
        return entry(WEB_DRIVER_TO_LAUNCH, supportedWebDriver.name());
    }

    @DataProvider
    public Object[][] testData() {
        return new Object[][]{
                {ANY, Map.ofEntries(desiredDriver(CHROME_DRIVER)), ChromeDriver.class},
                {ANY, Map.ofEntries(desiredDriver(FIREFOX_DRIVER)), FirefoxDriver.class},
                {ANY, Map.ofEntries(desiredDriver(PHANTOM_JS_DRIVER)), PhantomJSDriver.class},
                {WIN8, Map.ofEntries(desiredDriver(IE_DRIVER)), InternetExplorerDriver.class},
                {WIN10, Map.ofEntries(desiredDriver(EDGE_DRIVER)), EdgeDriver.class},

        };
    }

    @Test(dataProvider = "testData")
    public void testOfTheStarting(Platform targetPlatform, Map<String, String> propertiesToSet,
                                  Class<? extends WebDriver> expectedWebDriver) {
        if (!getCurrent().is(targetPlatform)) {
            return;
        }
        propertiesToSet.forEach(System::setProperty);

        try {
            WrappedWebDriver wrappedWebDriver = (WrappedWebDriver)
                    new SeleniumParameterProvider().provide().getParameterValues()[0];
            WebDriver driver = null;
            try {
                driver = wrappedWebDriver.getWrappedDriver();
                assertThat("Check class of web driver", driver.getClass(), equalTo(expectedWebDriver));
                assertThat("Web driver is alive", isDriverAlive(driver), is(true));
            }
            finally {
                wrappedWebDriver.shutDown();
                ofNullable(driver).ifPresent(webDriver ->
                        assertThat("Web driver is dead", !isDriverAlive(webDriver), is(true)));
            }
        }
        finally {
            propertiesToSet.keySet().forEach(s -> System.getProperties().remove(s));
        }
    }
}
