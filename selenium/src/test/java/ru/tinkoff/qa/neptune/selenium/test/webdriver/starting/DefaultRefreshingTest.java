package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

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

import static java.lang.System.setProperty;
import static java.util.List.of;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WebDriverTunersProperty.WEB_DRIVER_TUNERS_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.test.webdriver.starting.TestWebDriverTunerSupplier.TEST_WEB_DRIVER_TUNER;

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
 *
 * Requirements:
 * Installed Chrome
 */
public class DefaultRefreshingTest {

    private final String SELENIUM = "https://github.com/SeleniumHQ/selenium";

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getName(), "https://github.com"),
                    entry(CHROME.getName(), ChromeSettingsSupplierHeadless.class.getName()));

    private final List<PropertySupplier<?>> PROPS = of(KEEP_WEB_DRIVER_SESSION_OPENED, WEB_DRIVER_TUNERS_PROPERTY);

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
        TEST_WEB_DRIVER_TUNER.actions.clear();
        PROPS.forEach(s -> System.getProperties().remove(s.getName()));
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

    @Test
    public void test4() {
        setProperty(WEB_DRIVER_TUNERS_PROPERTY.getName(), TestWebDriverTunerSupplier.class.getName());
        wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        wrappedWebDriver.getWrappedDriver();
        wrappedWebDriver.refreshContext();
        wrappedWebDriver.getWrappedDriver();


        assertThat("Registered tune actions",
                TEST_WEB_DRIVER_TUNER.actions,
                contains("created",
                        "created"));
    }

    @Test
    public void test5() {
        setProperty(WEB_DRIVER_TUNERS_PROPERTY.getName(), TestWebDriverTunerSupplier.class.getName());
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getName(), "true");

        wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        wrappedWebDriver.getWrappedDriver();
        wrappedWebDriver.refreshContext();
        wrappedWebDriver.getWrappedDriver();

        assertThat("Registered tune actions",
                TEST_WEB_DRIVER_TUNER.actions,
                contains("created",
                        "refreshed"));
    }

    @Test
    public void test6() {
        setProperty(WEB_DRIVER_TUNERS_PROPERTY.getName(), TestWebDriverTunerSupplier.class.getName());
        wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);

        wrappedWebDriver.getWrappedDriver();
        wrappedWebDriver.shutDown();
        wrappedWebDriver.getWrappedDriver();

        assertThat("Registered tune actions",
                TEST_WEB_DRIVER_TUNER.actions,
                contains("created",
                        "created"));
    }

    @Test
    public void test7() {
        setProperty(WEB_DRIVER_TUNERS_PROPERTY.getName(), TestWebDriverTunerSupplier.class.getName());
        setProperty(KEEP_WEB_DRIVER_SESSION_OPENED.getName(), "true");

        wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        wrappedWebDriver.getWrappedDriver();
        wrappedWebDriver.shutDown();
        wrappedWebDriver.refreshContext();
        wrappedWebDriver.getWrappedDriver();

        assertThat("Registered tune actions",
                TEST_WEB_DRIVER_TUNER.actions,
                contains("created",
                        "created"));
    }


    @AfterMethod
    public void afterTest() {
        ofNullable(wrappedWebDriver).ifPresent(WrappedWebDriver::shutDown);
    }

    @AfterClass
    public void tearDown() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
        PROPS.forEach(s -> System.getProperties().remove(s));
    }
}
