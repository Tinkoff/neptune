package ru.tinkoff.qa.neptune.selenium.test.browser.proxy.step;

import com.browserup.harreader.model.HarEntry;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierForProxy;

import java.util.List;
import java.util.Map;

import static com.browserup.harreader.model.HttpMethod.GET;
import static java.lang.Thread.sleep;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasMethod.requestHasMethod;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasUrl.requestHasUrl;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.ResponseHasStatusCode.responseHasStatusCode;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public class BrowserProxyStepTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getName(), "https://www.google.ru"),
                    entry(USE_BROWSER_PROXY.getName(), "true"),
                    entry(CHROME.getName(), ChromeSettingsSupplierForProxy.class.getName())
            );

    private SeleniumStepContext seleniumSteps;

    @BeforeMethod
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);

        seleniumSteps = new SeleniumStepContext((SupportedWebDrivers)
                new SeleniumParameterProvider().provide().getParameterValues()[0]);
    }

    @Test
    public void proxiedHarCaptureTest() {
        seleniumSteps.getWrappedDriver();
        List<HarEntry> requests = seleniumSteps.get(proxiedRequests());

        assertThat("Captured requests size is greater than 0", requests, hasSize(greaterThan(0)));
    }

    @Test
    public void proxyGetStepSupplierCriteriaTest() throws InterruptedException {
        seleniumSteps.getWrappedDriver();

        seleniumSteps.startNewProxyRecording()
                .refresh();

        sleep(5000);

        List<HarEntry> requests = seleniumSteps
                .get(proxiedRequests()
                        .criteria(requestMethod(GET))
                        .criteria(responseStatusCode(200))
                        .criteria(requestUrl("https://www.google.ru/")));

        assertThat("Proxy with filter captured only one request", requests, hasSize(1));
        assertThat("Captured entries have GET HTTP, status code 200 and same url", requests,
                allOf(
                        everyItem(requestHasMethod(GET)),
                        everyItem(responseHasStatusCode(200)),
                        everyItem(requestHasUrl("https://www.google.ru/"))
                ));
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
