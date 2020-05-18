package ru.tinkoff.qa.neptune.selenium.test.browser.proxy.step;

import com.browserup.harreader.model.HarEntry;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public class BrowserProxyStepTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getPropertyName(), CHROME_DRIVER.name()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getPropertyName(), "https://www.google.ru"),
                    entry(USE_BROWSER_PROXY.getPropertyName(), "true"),
                    entry(KEEP_WEB_DRIVER_SESSION_OPENED.getPropertyName(), "true")
//                    entry(CHROME.getPropertyName(), ChromeSettingsSupplierHeadless.class.getName())
            );

    private SeleniumStepContext seleniumSteps;

    @BeforeMethod
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);

        seleniumSteps = new SeleniumStepContext((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
    }

    @AfterClass(alwaysRun = true)
    public void tearDownProperties() {
        PROPERTIES_TO_SET_BEFORE.keySet().forEach(s -> System.getProperties().remove(s));
    }

    @Test
    public void proxiedHarCaptureTest() {
        seleniumSteps.getWrappedDriver();
        List<HarEntry> requests = seleniumSteps.get(proxiedRequests());

        assertThat("Captured requests size is greater than 0", requests, hasSize(greaterThan(0)));
    }
}
