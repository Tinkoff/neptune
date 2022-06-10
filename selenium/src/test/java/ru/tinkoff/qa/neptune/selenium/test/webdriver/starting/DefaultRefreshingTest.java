package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;

import java.net.URL;

import static java.lang.System.setProperty;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WebDriverCredentialsProperty.WEB_DRIVER_CREDENTIALS_PROPERTY;

@SuppressWarnings("unchecked")
public class DefaultRefreshingTest {

    private final String SELENIUM = "https://github.com/SeleniumHQ/selenium";

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
        wrappedWebDriver = new WrappedWebDriver(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.get());
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
    public void setUp() throws Exception {
        SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.accept(CHROME_DRIVER);
        BASE_WEB_DRIVER_URL_PROPERTY.accept(new URL("https://github.com"));
        CHROME.accept(new Class[]{ChromeSettingsSupplierHeadless.class});

    }

    @BeforeMethod
    public void beforeTest() {
        KEEP_WEB_DRIVER_SESSION_OPENED.accept(null);
        WEB_DRIVER_CREDENTIALS_PROPERTY.accept(null);
    }

    @Test
    public void test1() {
        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refreshContext();
        assertThat("Is driver dead", !isDriverAlive(webDriver), is(true));
    }

    @Test
    public void test2() {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getName(), "false");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refreshContext();
        assertThat("Is driver dead", !isDriverAlive(webDriver), is(true));
    }

    @Test
    public void test3() {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refreshContext();

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
    }


    @AfterMethod
    public void afterTest() {
        ofNullable(wrappedWebDriver).ifPresent(WrappedWebDriver::shutDown);
    }

    @AfterClass
    public void tearDown() {
        SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.accept(null);
        BASE_WEB_DRIVER_URL_PROPERTY.accept(null);
        CHROME.accept(null);

        KEEP_WEB_DRIVER_SESSION_OPENED.accept(null);
        WEB_DRIVER_CREDENTIALS_PROPERTY.accept(null);
    }
}
