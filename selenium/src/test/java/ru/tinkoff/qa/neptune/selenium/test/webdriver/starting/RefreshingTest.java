package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.setProperty;
import static java.lang.Thread.sleep;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.CLEAR_WEB_DRIVER_COOKIES;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

/**
 * This is the integration test which is supposed to be run on some local environment.
 * Goals:
 *
 * to make sure that {@link WrappedWebDriver#refreshContext()} works as expected
 *
 * to make sure that {@link WrappedWebDriver} doesn't ignore
 * {@link SessionFlagProperties#KEEP_WEB_DRIVER_SESSION_OPENED}
 *
 * to make sure that {@link WrappedWebDriver} doesn't ignore
 * {@link SessionFlagProperties#CLEAR_WEB_DRIVER_COOKIES}
 *
 * to make sure that {@link WrappedWebDriver} doesn't ignore
 *
 * Requirements:
 * Installed Chrome
 */
public class RefreshingTest {

    private final String SELENIUM = "https://github.com/SeleniumHQ/selenium";

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getName(), "https://github.com"),
                    entry(CHROME.getName(), ChromeSettingsSupplierHeadless.class.getName()));

    private final List<PropertySupplier<Boolean>> FLAGS =
            List.of(KEEP_WEB_DRIVER_SESSION_OPENED,
                    CLEAR_WEB_DRIVER_COOKIES);

    private WrappedWebDriver wrappedWebDriver;

    private static boolean isDriverAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        }
        catch (WebDriverException e) {
            return false;
        }
    }

    private WebDriver prepareWrappedWebDriver() {
        wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver toReturn = wrappedWebDriver.getWrappedDriver();
        String GITHUB = "https://github.com/";
        assertThat("Current url",
                toReturn.getCurrentUrl(),
                is(GITHUB));

        toReturn.get(SELENIUM);
        assertThat("Current url",
                toReturn.getCurrentUrl(),
                is(SELENIUM));

        return toReturn;
    }

    @BeforeClass
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);
    }

    @BeforeMethod
    public void beforeTest() {
        FLAGS.forEach(s -> System.getProperties().remove(s.getName()));
    }

    @Test(priority = 1)
    public void nothingIsDefinedTest() {
        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refreshContext();
        assertThat("Is driver dead", !isDriverAlive(webDriver), is(true));
    }

    @Test(priority = 1)
    public void toNotKeepSessionOpenedTest() {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getName(), "false");
        setProperty(CLEAR_WEB_DRIVER_COOKIES.getName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refreshContext();
        assertThat("Is driver dead", !isDriverAlive(webDriver), is(true));
    }

    @Test(priority = 1)
    public void toKeepSessionAliveWithNoOtherOptionTest() throws InterruptedException {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refreshContext();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));

        setProperty(CLEAR_WEB_DRIVER_COOKIES.getName(), "false");
        wrappedWebDriver.refreshContext();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));
    }

    @Test(priority = 1)
    public void toKeepSessionAliveWithCookieRemovalTest() throws InterruptedException {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getName(), "true");
        setProperty(CLEAR_WEB_DRIVER_COOKIES.getName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        Set<Cookie> cookies = webDriver.manage().getCookies();
        wrappedWebDriver.refreshContext();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                lessThan(cookies.size()));

        webDriver.get(SELENIUM);
        wrappedWebDriver.refreshContext();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                lessThan(cookies.size()));
    }

    @AfterMethod
    public void afterTest() {
        ofNullable(wrappedWebDriver).ifPresent(WrappedWebDriver::shutDown);
    }

    @AfterClass
    public void tearDown() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
        FLAGS.forEach(s -> System.getProperties().remove(s));
    }
}
