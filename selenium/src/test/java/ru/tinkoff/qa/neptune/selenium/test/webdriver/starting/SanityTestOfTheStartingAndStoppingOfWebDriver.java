package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;
import static java.lang.String.format;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.net.PortProber.findFreePort;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.BROWSER_NAME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.REMOTE;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.REMOTE_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;

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

    private final static String DEFAULT_LOCAL_HOST = "http://localhost:%s/wd/hub";

    private static boolean isDriverAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    /*private static Map.Entry<String, String> desiredDriver(SupportedWebDrivers supportedWebDriver) {
        return entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), supportedWebDriver.name());
    }

    private static Map.Entry<String, String> browserType(String browserType) {
        return entry(BROWSER_NAME.getName(), browserType);
    }*/

    private static SeleniumServer startServer(int port) {
        try {
            StandaloneConfiguration standAloneConfig = new StandaloneConfiguration();
            standAloneConfig.port = port;
            SeleniumServer server = new SeleniumServer(standAloneConfig);
            server.boot();
            return server;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @DataProvider
    public Object[][] testData() {
        return new Object[][]{
                {ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                        entry(CHROME.getName(), ChromeSettingsSupplierHeadless.class.getName())),
                        ChromeDriver.class, null},
                {ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), REMOTE_DRIVER.name()),
                        entry(REMOTE.getName(), ChromeSettingsSupplierHeadless.class.getName()),
                        entry(BROWSER_NAME.getName(), BrowserType.CHROME)), RemoteWebDriver.class,
                        BrowserType.CHROME},

        };
    }

    @Test(dataProvider = "testData")
    public void testOfTheStarting(Map<String, String> propertiesToSet,
                                  Class<? extends WebDriver> expectedWebDriver, String expectedBrowserType) {
        propertiesToSet.forEach(System::setProperty);

        try {
            WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                    new SeleniumParameterProvider().provide().getParameterValues()[0]);
            WebDriver driver = null;
            try {
                driver = wrappedWebDriver.getWrappedDriver();
                assertThat("Check class of web driver", expectedWebDriver.isAssignableFrom(driver.getClass()),
                        is(true));
                assertThat("Web driver is alive", isDriverAlive(driver), is(true));
                assertThat("Current url", driver.getCurrentUrl(), anyOf(is("about:blank"), is("data:,")));
                WebDriver toCheckCapabilities = driver;
                ofNullable(expectedBrowserType).ifPresent(s -> assertThat("Browser type from returned capabilities",
                        ((HasCapabilities) toCheckCapabilities).getCapabilities().getBrowserName(),
                        is(expectedBrowserType)));
            } finally {
                wrappedWebDriver.shutDown();
                ofNullable(driver).ifPresent(webDriver ->
                        assertThat("Web driver is dead", !isDriverAlive(webDriver), is(true)));
            }
        } finally {
            propertiesToSet.keySet().forEach(s -> System.getProperties().remove(s));
        }
    }

    @Test
    public void testOfTheRemoteStartingWithRemoteURL() throws Exception {
        SeleniumServer server;
        WrappedWebDriver wrappedWebDriver = null;
        int port = findFreePort();
        URL url = new URL(format(DEFAULT_LOCAL_HOST, port));

        Map<String, String> properties = new HashMap<>(ofEntries(
                entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), REMOTE_DRIVER.name()),
                entry(REMOTE.getName(), ChromeSettingsSupplierHeadless.class.getName()),
                entry(BROWSER_NAME.getName(), BrowserType.CHROME),
                entry(REMOTE_WEB_DRIVER_URL_PROPERTY.getName(), url.toString())));
        properties.forEach(System::setProperty);
        server = startServer(port);

        SupportedWebDrivers supportedWebDriver = SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.get();
        Object[] parameters = supportedWebDriver.get();

        try {
            assertThat("Check that web driver manager is null", supportedWebDriver.getWebDriverManager(),
                    nullValue());
            assertThat("Starting parameters contain remote URL as defined according to server settings. Also " +
                            "parameters should contain capabilities.",
                    parameters, arrayContaining(is(url), instanceOf(Capabilities.class)));

            chromedriver().setup();

            wrappedWebDriver = new WrappedWebDriver(supportedWebDriver);
            WebDriver driver = wrappedWebDriver.getWrappedDriver();
            assertThat("Web driver is alive", isDriverAlive(driver), is(true));
            assertThat("Current url", driver.getCurrentUrl(), anyOf(is("about:blank"),
                    is("data:,")));
        } finally {
            ofNullable(wrappedWebDriver).ifPresent(WrappedWebDriver::shutDown);
            ofNullable(server).ifPresent(SeleniumServer::stop);
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
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
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
