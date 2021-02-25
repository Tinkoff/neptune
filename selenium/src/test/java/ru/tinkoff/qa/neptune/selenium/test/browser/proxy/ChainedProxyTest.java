package ru.tinkoff.qa.neptune.selenium.test.browser.proxy;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierForProxy;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierWithDefinedProxy;

import java.util.Map;

import static com.browserup.bup.proxy.CaptureType.*;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.PROXY_URL_PROPERTY;

public class ChainedProxyTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(CHROME.getName(), ChromeSettingsSupplierForProxy.class.getName()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getName(), "https://www.google.com"),
                    entry(USE_BROWSER_PROXY.getName(), "true"));

    private static boolean isDriverAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    private BrowserUpProxy externalProxy;

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {CHROME.getName(), ChromeSettingsSupplierWithDefinedProxy.class.getName()},
                {PROXY_URL_PROPERTY.getName(), "http://127.0.0.1:8089"}
        };
    }

    @BeforeClass
    public void setUpProxy() {
        externalProxy = new BrowserUpProxyServer();
        externalProxy.setTrustAllServers(true);
        externalProxy.enableHarCaptureTypes(REQUEST_HEADERS, REQUEST_CONTENT, REQUEST_COOKIES,
                RESPONSE_HEADERS, RESPONSE_CONTENT, RESPONSE_COOKIES);
        externalProxy.start(8089);
    }

    @BeforeMethod
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);

        externalProxy.newHar();
    }

    @Test(dataProvider = "testData")
    public void trafficCaptureWithPredefinedUpstreamProxyTest(String propertyName, String propertyValue) {
        System.setProperty(propertyName, propertyValue);

        WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        try {
            assertThat("WebDriver is alive", isDriverAlive(driver), is(true));
            assertThat("BrowserUp proxy is instantiated", wrappedWebDriver.getProxy(), notNullValue());
            assertThat("BrowserUp proxy server is started", wrappedWebDriver.getProxy().isStarted(), is(true));

            assertThat("Internal proxy captured browser traffic",
                    wrappedWebDriver.getProxy().getHar().getLog().getEntries(),
                    hasSize(greaterThan(0)));

            assertThat("External proxy captured browser traffic",
                    externalProxy.getHar().getLog().getEntries(),
                    hasSize(greaterThan(0)));
        } finally {
            wrappedWebDriver.shutDown();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownProperties() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
    }
}
