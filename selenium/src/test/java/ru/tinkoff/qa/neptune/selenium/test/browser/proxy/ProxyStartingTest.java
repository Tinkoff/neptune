package ru.tinkoff.qa.neptune.selenium.test.browser.proxy;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierForProxy;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.selenium.BrowserProxy.getCurrentProxy;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;

public class ProxyStartingTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(CHROME.getName(), ChromeSettingsSupplierForProxy.class.getName()));

    private static boolean isDriverAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {true, notNullValue()},
                {false, nullValue()},
                {null, nullValue()}
        };
    }

    @BeforeMethod
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);
    }

    @Test(dataProvider = "testData", description = "Test of proxy properties")
    public void test1(Boolean propertyValue, Matcher<Object> proxyMatcher) {
        if (propertyValue != null) {
            USE_BROWSER_PROXY.accept(propertyValue);
        }

        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        try {
            assertThat("WebDriver is alive", isDriverAlive(driver), is(true));
            assertThat("BrowserUp proxy is instantiated", getCurrentProxy(), proxyMatcher);

            if (getCurrentProxy() != null) {
                assertThat("BrowserUp proxy server is started", getCurrentProxy().isStarted(), is(true));
            }
        } finally {
            wrappedWebDriver.shutDown();
        }
    }

    @Test(description = "when browser context is refreshed")
    public void test2() {
        USE_BROWSER_PROXY.accept(true);
        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        driver.get("https://google.com");

        wrappedWebDriver.refreshContext();

        assertThat("Browser proxy",
                getCurrentProxy().isStarted(),
                is(false));
    }

    @Test(threadPoolSize = 5, invocationCount = 5, description = "multiple thread test")
    public void test3() {
        USE_BROWSER_PROXY.accept(true);
        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        driver.get("https://google.com");

        assertThat("Browser proxy",
                getCurrentProxy().isStarted(),
                is(true));
    }

    @AfterClass(alwaysRun = true)
    public void tearDownProperties() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
        USE_BROWSER_PROXY.accept(null);
    }
}
