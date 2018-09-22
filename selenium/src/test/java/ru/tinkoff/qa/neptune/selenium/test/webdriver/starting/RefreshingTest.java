package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;

import static ru.tinkoff.qa.neptune.selenium.properties.FlagProperties.CLEAR_WEB_DRIVER_COOKIES;
import static ru.tinkoff.qa.neptune.selenium.properties.FlagProperties.GET_BACK_TO_BASE_URL;
import static ru.tinkoff.qa.neptune.selenium.properties.FlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.WEB_DRIVER_TO_LAUNCH;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static java.lang.System.setProperty;
import static java.lang.Thread.sleep;
import static java.util.Map.entry;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * This is the integration test which is supposed to be run on some local environment.
 * Goals:
 *
 * to make sure that {@link WrappedWebDriver#refresh()} works as expected
 *
 * to make sure that {@link WrappedWebDriver} doesn't ignore
 * {@link ru.tinkoff.qa.neptune.selenium.properties.FlagProperties#KEEP_WEB_DRIVER_SESSION_OPENED}
 *
 * to make sure that {@link WrappedWebDriver} doesn't ignore
 * {@link ru.tinkoff.qa.neptune.selenium.properties.FlagProperties#CLEAR_WEB_DRIVER_COOKIES}
 *
 * to make sure that {@link WrappedWebDriver} doesn't ignore
 * {@link ru.tinkoff.qa.neptune.selenium.properties.FlagProperties#GET_BACK_TO_BASE_URL}
 *
 * Requirements:
 * Installed Chrome
 */
public class RefreshingTest {

    private final String SELENIUM = "https://github.com/SeleniumHQ/selenium";
    private final String GITHUB = "https://github.com/";

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            Map.ofEntries(entry(WEB_DRIVER_TO_LAUNCH, CHROME_DRIVER.name()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "https://github.com"));

    private final List<PropertySupplier<Boolean>> FLAGS =
            List.of(KEEP_WEB_DRIVER_SESSION_OPENED,
                    CLEAR_WEB_DRIVER_COOKIES,
                    GET_BACK_TO_BASE_URL);

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
        FLAGS.forEach(s -> System.getProperties().remove(s.getPropertyName()));
    }

    @Test
    public void nothingIsDefinedTest() {
        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refresh();
        assertThat("Is driver dead", !isDriverAlive(webDriver), is(true));
    }

    @Test
    public void toNotKeepSessionOpenedTest() {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "false");
        setProperty(CLEAR_WEB_DRIVER_COOKIES.getPropertyName(), "true");
        setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refresh();
        assertThat("Is driver dead", !isDriverAlive(webDriver), is(true));
    }

    @Test
    public void toKeepSessionAliveWithNoOtherOptionTest() throws InterruptedException {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));

        setProperty(CLEAR_WEB_DRIVER_COOKIES.getPropertyName(), "false");
        setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "false");
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));
    }

    @Test
    public void toKeepSessionAliveWithGettingBackToBaseUrlTest() throws InterruptedException {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "true");
        setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(GITHUB));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));

        setProperty(CLEAR_WEB_DRIVER_COOKIES.getPropertyName(), "false");
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(GITHUB));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));
    }

    @Test
    public void toKeepSessionAliveWithCookieRemovalTest() throws InterruptedException {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "true");
        setProperty(CLEAR_WEB_DRIVER_COOKIES.getPropertyName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                either(is(0)).or(is(1)));

        setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "false");
        webDriver.get(SELENIUM);
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                either(is(0)).or(is(1)));
    }

    @Test
    public void dynamicalChangeOfOptionsTest() throws InterruptedException {
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "true");

        WebDriver webDriver = prepareWrappedWebDriver();
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));

        setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "true");
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(GITHUB));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                greaterThan(0));

        webDriver.get(SELENIUM);
        setProperty(GET_BACK_TO_BASE_URL.getPropertyName(), "false");
        setProperty(CLEAR_WEB_DRIVER_COOKIES.getPropertyName(), "true");
        wrappedWebDriver.refresh();
        sleep(1000);

        assertThat("Is driver alive", isDriverAlive(webDriver), is(true));
        assertThat("Current url",
                webDriver.getCurrentUrl(),
                is(SELENIUM));
        assertThat("Are cookies there",
                webDriver.manage().getCookies().size(),
                either(is(0)).or(is(1)));

        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "false");
        wrappedWebDriver.refresh();
        assertThat("Is driver dead", !isDriverAlive(webDriver), is(true));
    }

    @AfterMethod
    public void afterTest() {
        ofNullable(wrappedWebDriver).ifPresent(WrappedWebDriver::close);
    }

    @AfterClass
    public void tearDown() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
        FLAGS.forEach(s -> System.getProperties().remove(s));
    }
}
