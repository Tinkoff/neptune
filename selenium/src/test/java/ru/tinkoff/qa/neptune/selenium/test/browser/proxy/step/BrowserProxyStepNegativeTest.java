package ru.tinkoff.qa.neptune.selenium.test.browser.proxy.step;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierForProxy;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public class BrowserProxyStepNegativeTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getPropertyName(), CHROME_DRIVER.name()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "https://www.google.ru"),
                    entry(USE_BROWSER_PROXY.getPropertyName(), "false"),
                    entry(CHROME.getPropertyName(), ChromeSettingsSupplierForProxy.class.getName())
            );

    private SeleniumStepContext seleniumSteps;

    @BeforeMethod
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);

        seleniumSteps = new SeleniumStepContext((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
    }

    @Test
    public void harCaptureWhenProxyIsDisabledTest() {
        seleniumSteps.getWrappedDriver();

        assertThat("List of captured requests is empty", seleniumSteps.get(proxiedRequests()), hasSize(0));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownDriver() {
        seleniumSteps.stop();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownProperties() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
    }
}
