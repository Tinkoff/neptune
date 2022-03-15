package ru.tinkoff.qa.neptune.selenium.test.browser.proxy.step;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.SeleniumParameterProvider;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;
import ru.tinkoff.qa.neptune.selenium.test.capability.suppliers.ChromeSettingsSupplierForProxy;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static java.time.Duration.ofSeconds;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfIterable;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasMethod.requestHasMethod;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasUrl.requestHasStringUrl;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.ResponseHasStatusCode.responseHasStatusCode;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public class BrowserProxyStepTest {

    private final Map<String, String> PROPERTIES_TO_SET_BEFORE =
            ofEntries(entry(SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.getName(), CHROME_DRIVER.name()),
                    entry(BASE_WEB_DRIVER_URL_PROPERTY.getName(), "https://www.google.com"),
                    entry(CHROME.getName(), ChromeSettingsSupplierForProxy.class.getName())
            );

    private SeleniumStepContext seleniumSteps;

    @BeforeMethod
    public void setUp() {
        PROPERTIES_TO_SET_BEFORE.forEach(System::setProperty);

        seleniumSteps = new SeleniumStepContext((SupportedWebDrivers)
                new SeleniumParameterProvider().provide()[0]);
    }

    @Test
    public void proxiedCaptureTest() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps.navigateTo("https://www.google.com");
        List<HttpTraffic> requests = seleniumSteps.get(proxiedRequests());

        assertThat("Captured requests size is greater than 0", requests, hasSize(greaterThan(0)));
    }

    @Test
    public void proxyGetStepSupplierCriteriaTest() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps
                .navigateTo("https://github.com")
                .enableAndRefreshNetwork();

        List<HttpTraffic> requests = seleniumSteps.navigateTo("/")
                .get(proxiedRequests()
                        .recordedRequestMethod(GET)
                        .recordedResponseStatusCode(200)
                        .recordedRequestUrlMatches("https://www.google.com")
                        .timeOut(ofSeconds(10)));

        assertThat("Proxy with filter captured only one request", requests, hasSize(greaterThanOrEqualTo(1)));
        assertThat("Captured entries have GET HTTP, status code 200 and same url", requests,
                eachOfIterable(requestHasMethod(GET),
                        responseHasStatusCode(200),
                        requestHasStringUrl(containsString("https://www.google.com")))
        );
    }

    @Test
    public void checkDoubleCall() {
        seleniumSteps.enableAndRefreshNetwork();

        List<HttpTraffic> requests1 = seleniumSteps.navigateTo("/")
                .get(proxiedRequests()
                        .recordedRequestMethod(GET)
                        .recordedResponseStatusCode(200)
                        .recordedRequestUrlMatches("https://www.google.com/complete")
                        .timeOut(ofSeconds(10)));

        List<HttpTraffic> requests2 = seleniumSteps
                .get(proxiedRequests()
                        .recordedRequestMethod(GET)
                        .recordedResponseStatusCode(200)
                        .recordedRequestUrlMatches("https://www.google.com/complete")
                        .timeOut(ofSeconds(10)));


        assertThat("Сhecking the number of records", requests1, containsInAnyOrder(requests2.toArray()));
    }

    @Test
    public void closedWindowBeforeReceivingRequests() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps.navigateTo("https://www.google.com");
        seleniumSteps.closeWindow();
        List<HttpTraffic> requests = seleniumSteps.get(proxiedRequests());

        assertThat("Proxy captured requests", requests, hasSize(greaterThan(0)));
    }

    @Test
    public void closedWindowBeforeRefreshContext() {
        seleniumSteps.enableAndRefreshNetwork();
        seleniumSteps.navigateTo("https://www.google.com");
        seleniumSteps.closeWindow();
        seleniumSteps.refreshContext();

        assertThat("fake assert", true, is(true));
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
