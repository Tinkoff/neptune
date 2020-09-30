package ru.tinkoff.qa.neptune.selenium.test.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierHeadless;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierWithDefinedProxy;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;

public class ProxyStartingTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(CHROME.getName(), ChromeSettingsSupplierHeadless.class.getName()));

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
                {"true", notNullValue()},
                {"false", nullValue()},
                {null, nullValue()}
        };
    }

    @BeforeMethod
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);
    }

    @Test(dataProvider = "testData")
    public void useBrowserProxyPropertyTest(String propertyValue, Matcher<Object> proxyMatcher) {
        if (propertyValue != null) {
            System.setProperty(USE_BROWSER_PROXY.getName(), propertyValue);
        }

        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        try {
            assertThat("WebDriver is alive", isDriverAlive(driver), is(true));
            assertThat("BrowserUp proxy is instantiated", wrappedWebDriver.getProxy(), proxyMatcher);

            if (wrappedWebDriver.getProxy() != null) {
                assertThat("BrowserUp proxy server is started", wrappedWebDriver.getProxy().isStarted(), is(true));
            }
        } finally {
            wrappedWebDriver.shutDown();
        }
    }

    @Test
    public void skipProxyIfAlreadyExistsTest() {
        System.setProperty(USE_BROWSER_PROXY.getName(), "true");
        System.setProperty(CHROME.getName(), ChromeSettingsSupplierWithDefinedProxy.class.getName());

        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        try {
            assertThat("WebDriver is alive", isDriverAlive(driver), is(true));
            assertThat("BrowserUp proxy is instantiated", wrappedWebDriver.getProxy(), notNullValue());
            assertThat("BrowserUp proxy server is not started", wrappedWebDriver.getProxy().isStarted(), is(false));
        } finally {
            wrappedWebDriver.shutDown();
        }
    }

    @Test
    public void refreshContextTest() {
        System.setProperty(USE_BROWSER_PROXY.getName(), "true");

        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        driver.get("https://yandex.ru");

        List<HarEntry> harEntries = wrappedWebDriver.getProxy().getHar().getLog().getEntries();

        wrappedWebDriver.refreshContext();

        assertThat("HAR entries list",
                wrappedWebDriver.getProxy().getHar().getLog().getEntries(),
                hasItems(not(harEntries.toArray())));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownProxyProperty() {
        System.getProperties().remove(USE_BROWSER_PROXY.getName());
    }

    @AfterClass(alwaysRun = true)
    public void tearDownProperties() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
    }
}
