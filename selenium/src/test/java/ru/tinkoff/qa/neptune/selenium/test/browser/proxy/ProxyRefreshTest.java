package ru.tinkoff.qa.neptune.selenium.test.browser.proxy;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.WrappedWebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierForProxy;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static ru.tinkoff.qa.neptune.selenium.BrowserProxy.getCurrentProxy;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;

public class ProxyRefreshTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(CHROME.getName(), ChromeSettingsSupplierForProxy.class.getName()),
                    entry(USE_BROWSER_PROXY.getName(), "true"));

    private final WrappedWebDriver wrappedWebDriver = new WrappedWebDriver((SupportedWebDrivers)
            new SeleniumParameterProvider().provide().getParameterValues()[0]);

    @BeforeClass
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);
    }

    @BeforeMethod
    public void refresh() {
        wrappedWebDriver.refreshContext();
    }

    @Test(invocationCount = 2)
    public void crossSessionProxyRestartTest() {
        WebDriver driver = wrappedWebDriver.getWrappedDriver();

        driver.get("https://google.com");

        assertThat("HAR entries list",
                getCurrentProxy().getHar().getLog().getEntries(),
                hasSize(greaterThan(0)));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        wrappedWebDriver.shutDown();

        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
    }
}
