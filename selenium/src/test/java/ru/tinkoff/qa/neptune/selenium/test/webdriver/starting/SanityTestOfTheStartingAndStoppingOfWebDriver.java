package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.BROWSER_NAME;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.WEB_DRIVER_TO_LAUNCH;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.*;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;
import static io.github.bonigarcia.wdm.WebDriverManager.phantomjs;
import static java.lang.String.format;
import static java.util.Map.entry;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.Platform.*;
import static org.openqa.selenium.net.PortProber.findFreePort;
import static org.openqa.selenium.remote.BrowserType.*;

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

    private final static String DEFAULT_LOCAL_HOST = "http://localhost:%s/wd/hub";

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

    private static Map.Entry<String, String> browserType(String browserType) {
        return entry(BROWSER_NAME.getPropertyName(), browserType);
    }

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
                {ANY, Map.ofEntries(desiredDriver(CHROME_DRIVER)), ChromeDriver.class, null},
                {ANY, Map.ofEntries(desiredDriver(FIREFOX_DRIVER)), FirefoxDriver.class, null},
                {ANY, Map.ofEntries(desiredDriver(PHANTOM_JS_DRIVER)), PhantomJSDriver.class, null},
                {WIN8, Map.ofEntries(desiredDriver(IE_DRIVER)), InternetExplorerDriver.class, null},

                {ANY, Map.ofEntries(desiredDriver(REMOTE_DRIVER),
                        browserType(CHROME)), RemoteWebDriver.class, CHROME},
                {ANY, Map.ofEntries(desiredDriver(REMOTE_DRIVER),
                        browserType(FIREFOX)), RemoteWebDriver.class, FIREFOX},
                {ANY, Map.ofEntries(desiredDriver(REMOTE_DRIVER),
                        browserType(PHANTOMJS)), RemoteWebDriver.class, PHANTOMJS},
                {WIN8, Map.ofEntries(desiredDriver(REMOTE_DRIVER),
                        browserType(IEXPLORE)), RemoteWebDriver.class, IEXPLORE}

        };
    }

    @Test(dataProvider = "testData")
    public void testOfTheStarting(Platform targetPlatform, Map<String, String> propertiesToSet,
                                  Class<? extends WebDriver> expectedWebDriver, String expectedBrowserType) {
        if (!getCurrent().is(targetPlatform)) {
            return;
        }
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

    @Test
    public void testOfTheRemoteStartingWithRemoteURL() throws Exception {
        SeleniumServer server;
        WrappedWebDriver wrappedWebDriver = null;
        int port = findFreePort();
        URL url = new URL(format(DEFAULT_LOCAL_HOST, port));

        Map<String, String> properties = new HashMap<>(Map.ofEntries(desiredDriver(REMOTE_DRIVER),
                browserType(PHANTOMJS),
                entry(REMOTE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), url.toString())));
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

            phantomjs().setup();

            wrappedWebDriver = new WrappedWebDriver(supportedWebDriver);
            WebDriver driver = wrappedWebDriver.getWrappedDriver();
            assertThat("Web driver is alive", isDriverAlive(driver), is(true));
            assertThat("Current url", driver.getCurrentUrl(), anyOf(is("about:blank"),
                    is("data:,")));
        }
        finally {
            ofNullable(wrappedWebDriver).ifPresent(WrappedWebDriver::shutDown);
            ofNullable(server).ifPresent(SeleniumServer::stop);
            properties.keySet().forEach(s -> System.getProperties().remove(s));
        }
    }

    @Test
    public void startSessionWithBaseURL() {
        Map<String, String> properties = new HashMap<>(Map.ofEntries(desiredDriver(CHROME_DRIVER),
                entry(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "https://github.com/")));
        properties.forEach(System::setProperty);

        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        try {
            assertThat("Current url",
                    wrappedWebDriver.getWrappedDriver().getCurrentUrl(),
                    is("https://github.com/"));
        }
        finally {
            wrappedWebDriver.shutDown();
            properties.keySet().forEach(s -> System.getProperties().remove(s));
        }
    }
}
